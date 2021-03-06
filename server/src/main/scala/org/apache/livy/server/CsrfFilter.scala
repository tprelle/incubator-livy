/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.livy.server

import javax.servlet._
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

class CsrfFilter extends Filter {

  val METHODS_TO_IGNORE = Set("GET", "OPTIONS", "HEAD");

  val HEADER_NAME = "X-Requested-By";

  override def init(filterConfig: FilterConfig): Unit = {}

  override def doFilter(request: ServletRequest,
                        response: ServletResponse,
                        chain: FilterChain): Unit = {
    val httpRequest = request.asInstanceOf[HttpServletRequest]

    if (!METHODS_TO_IGNORE.contains(httpRequest.getMethod)
      && httpRequest.getHeader(HEADER_NAME) == null) {
      response.asInstanceOf[HttpServletResponse].sendError(HttpServletResponse.SC_BAD_REQUEST,
        "Missing Required Header for CSRF protection.")
    } else {
      chain.doFilter(request, response)
    }
  }

  override def destroy(): Unit = {}
}

