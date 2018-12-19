/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.xperiall.http.server

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.{ IncomingConnection, ServerBinding }
import akka.http.scaladsl.server.Route._
import akka.stream.scaladsl.{ Sink, Source }
import com.xperiall.http.routes.Routes

import scala.concurrent.Future
import scala.io.StdIn

object Server extends App {

  import com.xperiall.http.config.ServerSettings._

  val server: Source[IncomingConnection, Future[ServerBinding]] =
    Http(actorSystem).bind(httpInterface, httpPort)

  log.info(s"\nAkka HTTP Server - Version ${actorSystem.settings.ConfigVersion} - running at http://$httpInterface:$httpPort/")

  val handler: Future[ServerBinding] =
    server
      .to(
        Sink.foreach {
          connection ⇒
            connection.handleWithAsyncHandler(asyncHandler(Routes.availableRoutes))
        })
      .run()

  handler.failed.foreach { case ex: Exception ⇒ log.error(ex, "Failed to bind to {}:{}", httpInterface, httpPort) }

  StdIn.readLine(s"\nPress RETURN to stop...")

  handler
    .flatMap(binding ⇒ binding.unbind())
    .onComplete(_ ⇒ actorSystem.terminate())
}
