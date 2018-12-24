/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.xperiall.http.server

import akka.http.scaladsl.Http
import com.xperiall.http.routes.Routes

object Server extends App {

  import com.xperiall.http.config.ServerSettings._

  val server =
    Http(actorSystem).bindAndHandle(Routes.availableRoutes, httpInterface, httpPort)

  log.info(s"\nAkka HTTP Server - Version ${actorSystem.settings.ConfigVersion} - running at http://$httpInterface:$httpPort/")
}