/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.xperiall.http.routes

import akka.http.scaladsl.coding.Gzip
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

trait BaseRoute {

  //For more information regarding the CORS directive, please https://github.com/lomigmegard/akka-http-cors
  def api(dsl: Route, prefix: Boolean, version: String = ""): Route =
    cors() {
      if (prefix) pathPrefix("api" / version)(encodeResponseWith(Gzip)(dsl))
      else encodeResponseWith(Gzip)(dsl)
    }
}
