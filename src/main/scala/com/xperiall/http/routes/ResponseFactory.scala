/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.xperiall.http.routes

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

import scala.concurrent.Future
import scala.util.{ Failure, Success }

trait ResponseFactory {

  import com.xperiall.http.config.ServerSettings._

  def sendResponse[T](eventualResult: Future[T])(implicit marshaller: T ⇒ ToResponseMarshallable): Route = {
    onComplete(eventualResult) {
      case Success(result) ⇒
        complete(result)
      case Failure(e) ⇒
        log.error(s"Error: ${e.toString}")
        complete(InternalServerError.toString())
    }
  }
}
