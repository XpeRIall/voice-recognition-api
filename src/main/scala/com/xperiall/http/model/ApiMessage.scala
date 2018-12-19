/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.xperiall.http.model

import akka.actor.ActorSystem
case class ApiMessage(message: String)

object ApiStatusMessages {

  def currentStatus()(implicit actorSystem: ActorSystem): String = {
    s"service: ${actorSystem.name} | uptime: ${(actorSystem.uptime.toFloat / 3600).formatted("%10.2f")} hours"
  }

  val unknownException = "An error occurred during the request."
}
