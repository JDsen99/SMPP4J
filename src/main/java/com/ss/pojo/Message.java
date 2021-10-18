/*
 *    Copyright (c) 2021 JDsen
 *    SMPP4J is licensed under Mulan PSL v2.
 *    You can use this software according to the terms and conditions of the Mulan PSL v2.
 *    You may obtain a copy of Mulan PSL v2 at:
 *             http://license.coscl.org.cn/MulanPSL2
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *    See the Mulan PSL v2 for more details.
 */
package com.ss.pojo;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/9/17-16:09
 */
public class Message implements Cloneable{
    private int id;
    private String phone;
    private String content;
    private String sendId;
    private int status;
    private String messageId;
    private int taskId;
    private String text;
    private int sendCount;

    public Message() {
    }

    public Message(int id, String phone, String content, String sendId, int status, String messageId, int taskId, String text, int sendCount) {
        this.id = id;
        this.phone = phone;
        this.content = content;
        this.sendId = sendId;
        this.status = status;
        this.messageId = messageId;
        this.taskId = taskId;
        this.text = text;
        this.sendCount = sendCount;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", content='" + content + '\'' +
                ", sendId='" + sendId + '\'' +
                ", status=" + status +
                ", messageId='" + messageId + '\'' +
                ", taskId=" + taskId +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
