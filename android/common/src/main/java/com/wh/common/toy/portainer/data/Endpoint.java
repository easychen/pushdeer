/**
 * Copyright 2021 json.cn
 */
package com.wh.common.toy.portainer.data;
import java.util.List;

/**
 * Auto-generated: 2021-12-07 23:38:6
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Endpoint {

    private int Id;
    private String Name;
    private int Type;
    private String URL;
    private int GroupId;
    private String PublicURL;
    private List<String> Extensions;
    private List<String> TagIds;
    private int Status;
    private String EdgeKey;
    private int EdgeCheckinInterval;
    private String ComposeSyntaxMaxVersion;
    private int LastCheckInDate;
    private String AuthorizedUsers;
    private String AuthorizedTeams;
    private String Tags;
    public void setId(int Id) {
        this.Id = Id;
    }
    public int getId() {
        return Id;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
    public String getName() {
        return Name;
    }

    public void setType(int Type) {
        this.Type = Type;
    }
    public int getType() {
        return Type;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
    public String getURL() {
        return URL;
    }

    public void setGroupId(int GroupId) {
        this.GroupId = GroupId;
    }
    public int getGroupId() {
        return GroupId;
    }

    public void setPublicURL(String PublicURL) {
        this.PublicURL = PublicURL;
    }
    public String getPublicURL() {
        return PublicURL;
    }

    public void setExtensions(List<String> Extensions) {
        this.Extensions = Extensions;
    }
    public List<String> getExtensions() {
        return Extensions;
    }

    public void setTagIds(List<String> TagIds) {
        this.TagIds = TagIds;
    }
    public List<String> getTagIds() {
        return TagIds;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }
    public int getStatus() {
        return Status;
    }

    public void setEdgeKey(String EdgeKey) {
        this.EdgeKey = EdgeKey;
    }
    public String getEdgeKey() {
        return EdgeKey;
    }

    public void setEdgeCheckinInterval(int EdgeCheckinInterval) {
        this.EdgeCheckinInterval = EdgeCheckinInterval;
    }
    public int getEdgeCheckinInterval() {
        return EdgeCheckinInterval;
    }

    public void setComposeSyntaxMaxVersion(String ComposeSyntaxMaxVersion) {
        this.ComposeSyntaxMaxVersion = ComposeSyntaxMaxVersion;
    }
    public String getComposeSyntaxMaxVersion() {
        return ComposeSyntaxMaxVersion;
    }

    public void setLastCheckInDate(int LastCheckInDate) {
        this.LastCheckInDate = LastCheckInDate;
    }
    public int getLastCheckInDate() {
        return LastCheckInDate;
    }

    public void setAuthorizedUsers(String AuthorizedUsers) {
        this.AuthorizedUsers = AuthorizedUsers;
    }
    public String getAuthorizedUsers() {
        return AuthorizedUsers;
    }

    public void setAuthorizedTeams(String AuthorizedTeams) {
        this.AuthorizedTeams = AuthorizedTeams;
    }
    public String getAuthorizedTeams() {
        return AuthorizedTeams;
    }

    public void setTags(String Tags) {
        this.Tags = Tags;
    }
    public String getTags() {
        return Tags;
    }

}