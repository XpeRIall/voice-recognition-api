/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.xperiall.http.config

import akka.actor.ActorSystem
import akka.event.{ LogSource, Logging, LoggingAdapter }
import akka.stream.ActorMaterializer
import com.typesafe.config.{ Config, ConfigFactory }

import scala.concurrent.ExecutionContextExecutor

trait ServerSettings {

  lazy private val config: Config = ConfigFactory.load()
  private val httpConfig: Config = config.getConfig("http")
  val httpInterface: String = httpConfig.getString("interface")
  val httpPort: Int = httpConfig.getInt("port")

  implicit val actorSystem: ActorSystem = ActorSystem("voice-recognition-api")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  private implicit val logSource: LogSource[ServerSettings] = (t: ServerSettings) ⇒ t.getClass.getSimpleName

  private def logger(implicit logSource: LogSource[_ <: ServerSettings]) = Logging(actorSystem, this.getClass)

  implicit val log: LoggingAdapter = logger
}

object ServerSettings extends ServerSettings
