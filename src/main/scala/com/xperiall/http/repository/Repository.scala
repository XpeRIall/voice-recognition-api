/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.xperiall.http.repository

import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding
import com.google.cloud.speech.v1.{ RecognitionAudio, RecognitionConfig, SpeechClient, SpeechRecognitionResult }
import com.google.protobuf.ByteString
import com.xperiall.http.model.{ Model, VoiceRecognitionRequest }

import scala.collection.JavaConverters._
import scala.collection.immutable.IndexedSeq
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success, Try }

object Repository {

  def getModels(amount: Int): Future[IndexedSeq[Model]] = Future {
    require(amount > 0, "The amount must be greater than zero.")
    (1 to amount).map { i ⇒
      Model(s"Model$i", i, i.toLong, i.toFloat, i.toDouble, Seq(1, 2, 3), List(1, 2, 3))
    }
  }

  def getModelsByName(name: String): Future[Model] = Future {
    require(name.nonEmpty, "The name must be present.")
    Model(s"Model-$name", 1, 1.toLong, 1.toFloat, 1.toDouble, Seq(1, 2, 3), List(1, 2, 3))
  }

  def ProcessVoiceCommand(voiceRecognitionRequest: VoiceRecognitionRequest): Either[String, Seq[SpeechRecognitionResult]] = {
    Try {
      val speechClient = SpeechClient create ()
      val audioBytes: ByteString = ByteString.copyFrom(voiceRecognitionRequest.data)

      val recognitionConfig = RecognitionConfig.newBuilder()
        .setEncoding(AudioEncoding.LINEAR16)
        .setLanguageCode("en-US")
        .setSampleRateHertz(16000)
        .setEnableWordTimeOffsets(true)
        .build()
      val recognitionAudio = RecognitionAudio.newBuilder()
        .setContent(audioBytes)
        .build()

      val response = speechClient.longRunningRecognizeAsync(recognitionConfig, recognitionAudio)

      while (!response.isDone) {
        println("Waiting for response...")
        Thread sleep 10000
      }
      response.get().getResultsList.asScala
    } match {
      case Success(value)     ⇒ Right(value)
      case Failure(exception) ⇒ Left(exception.getMessage)
    }
  }
}
