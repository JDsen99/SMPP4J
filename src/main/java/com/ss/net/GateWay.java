/**
 *    Copyright (c) [2021] [JDsen]
 *    [Software Name] is licensed under Mulan PSL v2.
 *    You can use this software according to the terms and conditions of the Mulan PSL v2.
 *    You may obtain a copy of Mulan PSL v2 at:
 *             http://license.coscl.org.cn/MulanPSL2
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *    See the Mulan PSL v2 for more details.
 */
package com.ss.net;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/10/18-20:50
 */
public interface GateWay {

    /**
     * 执行连接
     */
    void doConnect();

    /**
     * 展示状态
     */
    void showStatus();
}
