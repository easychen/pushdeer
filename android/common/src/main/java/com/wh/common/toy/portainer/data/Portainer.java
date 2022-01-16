/**
 * Copyright 2021 json.cn
 */
package com.wh.common.toy.portainer.data;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Auto-generated: 2021-12-07 13:3:57
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Portainer {

    @NonNull
    public String toString() {
        return getFirstName().replace("/", "") + " " + getState();
    }

    public String getFirstName() {
        return getNames().get(0);
    }

    //    private String Command;
    private long Created;
    //    private HostConfig HostConfig;
//    private String Id;
    private String Image;
    //    private String ImageID;
    //    private Labels Labels;
//    private List<Mounts> Mounts;
    private List<String> Names;
    //    private NetworkSettings NetworkSettings;
//    private List<Ports> Ports;
    private String State;
    private String Status;

//    public void setCommand(String Command) {
//        this.Command = Command;
//    }

//    public String getCommand() {
//        return Command;
//    }

    public void setCreated(long Created) {
        this.Created = Created;
    }

    public long getCreated() {
        return Created;
    }

//    public void setHostConfig(HostConfig HostConfig) {
//        this.HostConfig = HostConfig;
//    }

//    public HostConfig getHostConfig() {
//        return HostConfig;
//    }

//    public void setId(String Id) {
//        this.Id = Id;
//    }

//    public String getId() {
//        return Id;
//    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getImage() {
        return Image;
    }

//    public void setImageID(String ImageID) {
//        this.ImageID = ImageID;
//    }

//    public String getImageID() {
//        return ImageID;
//    }

//    public void setLabels(Labels Labels) {
//         this.Labels = Labels;
//     }
//     public Labels getLabels() {
//         return Labels;
//     }

//    public void setMounts(List<Mounts> Mounts) {
//        this.Mounts = Mounts;
//    }

//    public List<Mounts> getMounts() {
//        return Mounts;
//    }

    public void setNames(List<String> Names) {
        this.Names = Names;
    }

    public List<String> getNames() {
        return Names;
    }

//    public void setNetworkSettings(NetworkSettings NetworkSettings) {
//        this.NetworkSettings = NetworkSettings;
//    }

//    public NetworkSettings getNetworkSettings() {
//        return NetworkSettings;
//    }

//    public void setPorts(List<Ports> Ports) {
//        this.Ports = Ports;
//    }

//    public List<Ports> getPorts() {
//        return Ports;
//    }

    public void setState(String State) {
        this.State = State;
    }

    public String getState() {
        return State;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getStatus() {
        return Status;
    }

}