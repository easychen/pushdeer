package notify

import (
	"os"
	"testing"
)

//测试单推
func TestXmPush_SendByCid(t *testing.T) {
	iXmPush, err := NewXmPush(Cfg)
	if err != nil {
		t.Error(err)
		os.Exit(1)
	}

	cid := "xxx"
	payLoad := Payload{"这是测试title", "这是测试内容", "1", ""}
	err = iXmPush.SendByCid(cid, &payLoad)
	if err != nil {
		t.Error(err)
	} else {
		t.Log("ok")
	}
}

//测试群推
func TestXmPush_SendByCids(t *testing.T) {
	iXmPush, err := NewXmPush(Cfg)
	if err != nil {
		t.Error(err)
		os.Exit(1)
	}

	cids := []string{"xxx"}
	payLoad := Payload{"这是测试title", "这是测试内容", "1", ""}
	err = iXmPush.SendByCids(cids, &payLoad)
	if err != nil {
		t.Error(err)
	} else {
		t.Log("ok")
	}
}

//测试全推
func TestXmPush_SendAll(t *testing.T) {
	iXmPush, err := NewXmPush(Cfg)
	if err != nil {
		t.Error(err)
		os.Exit(1)
	}

	payLoad := Payload{"这是测试title", "这是测试内容", "1", ""}
	err = iXmPush.SendAll(&payLoad)
	if err != nil {
		t.Error(err)
	} else {
		t.Log("ok")
	}
}
