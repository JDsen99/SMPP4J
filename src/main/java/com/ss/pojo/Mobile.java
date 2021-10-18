/**
 *    Copyright (c) [2021] [JDsen]
 *    [Software Name] is licensed under Mulan PSL v2.
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
 * @createDate 2021/10/10-9:39
 */
public class Mobile {
    private int id;
    private String phone;
    private int taskId;
    private int status;

    public Mobile() {
    }

    public Mobile(int id, String phone, int taskId, int status) {
        this.id = id;
        this.phone = phone;
        this.taskId = taskId;
        this.status = status;
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

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Mobile{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                '}';
    }
}
