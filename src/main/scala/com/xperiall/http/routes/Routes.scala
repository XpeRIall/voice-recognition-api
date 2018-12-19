/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.xperiall.http.routes

import akka.http.scaladsl.server.Route
import com.google.cloud.speech.v1.SpeechRecognitionResult
import com.xperiall.http.model.{ ApiMessage, ApiStatusMessages, Model, VoiceRecognitionRequest }
import com.xperiall.http.repository.Repository
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import scala.concurrent.Future

object Routes extends BaseRoute with ResponseFactory {

  import akka.http.scaladsl.server.Directives._
  import FailFastCirceSupport._
  import io.circe.generic.auto._
  import com.xperiall.http.config.ServerSettings._

  protected def templateDirectives: Route =
    cors() {
      pathPrefix("service") {
        get {
          path("status") {
            extractRequest { _ ⇒
              sendResponse(Future(ApiMessage(ApiStatusMessages.currentStatus())))
            }
          }
        } ~
          post {
            path("recognize") {
              decodeRequest {
                entity(as[VoiceRecognitionRequest]) {
                  voiceRecognitionRequest ⇒
                    Repository.ProcessVoiceCommand(voiceRecognitionRequest) match {
                      case Right(res: Seq[SpeechRecognitionResult]) ⇒ sendResponse(Future(ApiMessage(res.mkString(","))))
                      case Left(ex)                                 ⇒ sendResponse(Future(ApiMessage(ex)))
                    }
                }
              }
            }
          }
      }
    }

  protected lazy val apiV1: Route =
    api(dsl = logRequestResult("log-service") {
      this.templateDirectives
    }, prefix = true, version = "v1")

  def availableRoutes: Route = apiV1

}
