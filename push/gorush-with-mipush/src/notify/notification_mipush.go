package notify

import (
	"context"
	"errors"
	"fmt"
	"github.com/appleboy/gorush/config"
	"github.com/appleboy/gorush/core"
	"github.com/appleboy/gorush/logx"
	"github.com/appleboy/gorush/status"
	"github.com/geek-go/xmpush"
	"strconv"
	"strings"
	"sync"
	"math/rand"
)

var (
	mipushError  error
	mipushClient *XMPush
	mipushOnce   sync.Once
)

// GetMIPushClient use for create HMS Push.
func GetMIPushClient(conf *XmpushConfig) (*XMPush, error) {
	once.Do(func() {
		client := &XMPush{
			Config: &XmpushConfig{
				AppSecret: conf.AppSecret,
				Package:   conf.Package,
			},
		}
		mipushClient = client
		pushError = nil
	})

	return mipushClient, pushError
}

func InitMIPUSHClient(cfg *config.ConfYaml, appSecret, pkg string) (*XMPush, error) {
	if appSecret == "" {
		return nil, errors.New("Missing MI App Secret")
	}

	if pkg == "" {
		return nil, errors.New("Missing MI App Package")
	}
	conf := &XmpushConfig{
		AppSecret: appSecret,
		Package:   pkg,
	}
	if appSecret != cfg.MI.AppSecret || pkg != cfg.MI.Package {
		return GetMIPushClient(conf)
	}
	if MIPUSHClient == nil {
		return GetMIPushClient(conf)
	}
	return MIPUSHClient, nil

}

func GetMINotification(req *PushNotification, pkg string) (*xmpush.Message, error) {
	payload := &Payload{
		PushTitle:    req.Title,
		PushBody:     req.Message,
		IsShowNotify: "1",
	}
	payloadStr, err := json.Marshal(payload)
	if err != nil {
		return nil, err
	}

	//是否透传
	passThrough := 1
	if payload.IsShowNotify == "1" {
		passThrough = 0 //通知栏消息
	}

	message := &xmpush.Message{
		Payload:               string(payloadStr),
		Title:                 payload.PushTitle,
		Description:           payload.PushBody,
		PassThrough:           int32(passThrough),
		NotifyType:            3,
		NotifyID:							 rand.Int63n(1024),
		RestrictedPackageName: pkg,
		Extra: map[string]string{
			"notify_effect": "1",
			"notify_foreground": "1",
			"channel_id":        "high_system",
			"channel_name":      "服务提醒",
		},
	}
	if len(req.Tokens) > 0 {
		for i, token := range req.Tokens {
			if i == 0 {
				message.RegistrationId = token
			} else {
				message.RegistrationId = message.RegistrationId + "," + token
			}
		}
	}
	return message, nil
}

func PushToMI(req *PushNotification, cfg *config.ConfYaml) (resp *ResponsePush, err error) {
	logx.LogAccess.Debug("Start push notification for MI")

	var (
		client     *XMPush
		retryCount = 0
		maxRetry   = cfg.MI.MaxRetry
	)

	if req.Retry > 0 && req.Retry < maxRetry {
		maxRetry = req.Retry
	}

	client, err = InitMIPUSHClient(cfg, cfg.MI.AppSecret, cfg.MI.Package)
	if err != nil {
		// MI server error
		logx.LogError.Error("MI server error: " + err.Error())
		return
	}

	resp = &ResponsePush{}

Retry:
	isError := false
	notification, err1 := GetMINotification(req, client.Config.Package)
	if err1 != nil {
		return nil, err1
	}

	res, err := client.SendMessage(context.Background(), notification)
	if err != nil {
		// Send Message error
		errLog := logPush(cfg, core.FailedPush, req.To, req, err)
		resp.Logs = append(resp.Logs, errLog)
		logx.LogError.Error("HMS server send message error: " + err.Error())
		return
	}
	// Huawei Push Send API does not support exact results for each token
	if res.Code == 0 {
		status.StatStorage.AddMISuccess(int64(1))
		logx.LogAccess.Debug("MI Send Notification is completed successfully!")
	} else {
		isError = true
		status.StatStorage.AddMIError(int64(1))
		logx.LogAccess.Debug("MI Send Notification is failed! Code: " + strconv.Itoa(int(res.Code)))
	}

	if isError && retryCount < maxRetry {
		retryCount++

		// resend all tokens
		goto Retry
	}

	return nil, nil
}

var Cfg = &XmpushConfig{
	AppSecret: "",
	Package:   "",
}

// XmpushConfig ...
type XmpushConfig struct {
	AppSecret string `toml:"app_secret"`
	Package   string `toml:"package"`
}

//Payload 消息payload，根据业务自定义
type Payload struct {
	PushTitle    string `json:"push_title"`
	PushBody     string `json:"push_body"`
	IsShowNotify string `json:"is_show_notify"`
	Ext          string `json:"ext"`
}

type XMPush struct {
	Config *XmpushConfig
}

// NewXmPush 获取实例
func NewXmPush(config *XmpushConfig) (*XMPush, error) {

	if config.Package == "" || config.AppSecret == "" {
		return nil, errors.New("please check config")
	}

	xm := &XMPush{
		Config: config,
	}

	return xm, nil
}

func (m *XMPush) SendMessage(ctx context.Context, notification *xmpush.Message) (*xmpush.Result, error) {
	// 通过 regID 推送
	if notification.RegistrationId != "" {
		return xmpush.SendMessageByRegIds(m.Config.AppSecret, notification)
	}
	// 群推
	return xmpush.SendMessageAll(m.Config.AppSecret, notification)

}

//SendByCid 根据用户cid推送
func (m *XMPush) SendByCid(cid string, payload *Payload) error {
	return m.SendByCids([]string{cid}, payload)
}

//SendByCids 根据用户cids批量推送
func (m *XMPush) SendByCids(cids []string, payload *Payload) error {

	payloadStr, _ := json.Marshal(payload)

	//是否透传
	passThrough := 1
	if payload.IsShowNotify == "1" {
		passThrough = 0 //通知栏消息
	}

	message := &xmpush.Message{
		Payload:               string(payloadStr),
		Title:                 payload.PushTitle,
		Description:           payload.PushBody,
		PassThrough:           int32(passThrough),
		NotifyType:            1,
		RestrictedPackageName: m.Config.Package,
		Extra: map[string]string{
			"notify_foreground": "1",
		},
	}

	message.RegistrationId = strings.Join(cids, ",")

	result, err := xmpush.SendMessageByRegIds(m.Config.AppSecret, message)
	if err != nil {
		return err
	}

	fmt.Println(result)

	return nil
}

// SendByAliases 根据别名推送批量推送
func (m *XMPush) SendByAliases(aliases []string, payload *Payload) error {

	payloadStr, _ := json.Marshal(payload)

	//是否透传
	passThrough := 1
	if payload.IsShowNotify == "1" {
		passThrough = 0 //通知栏消息
	}

	message := &xmpush.Message{
		Payload:               string(payloadStr),
		Title:                 payload.PushTitle,
		Description:           payload.PushBody,
		PassThrough:           int32(passThrough),
		NotifyType:            1,
		RestrictedPackageName: m.Config.Package,
		Extra: map[string]string{
			"notify_foreground": "1",
		},
	}

	message.Alias = strings.Join(aliases, ",")

	result, err := xmpush.SendMessageByRegAliasIds(m.Config.AppSecret, message)
	if err != nil {
		return err
	}

	fmt.Println(result)

	return nil
}

// SendAll 推送给所有人
func (m *XMPush) SendAll(payload *Payload) error {

	payloadStr, _ := json.Marshal(payload)

	//是否透传
	passThrough := 1
	if payload.IsShowNotify == "1" {
		passThrough = 0 //通知栏消息
	}

	message := &xmpush.Message{
		Payload:               string(payloadStr),
		Title:                 payload.PushTitle,
		Description:           payload.PushBody,
		PassThrough:           int32(passThrough),
		NotifyType:            1,
		RestrictedPackageName: m.Config.Package,
		Extra: map[string]string{
			"notify_foreground": "1",
		},
	}

	result, err := xmpush.SendMessageAll(m.Config.AppSecret, message)
	if err != nil {
		return err
	}

	fmt.Println(result)

	return nil
}
