/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.xperiall.http.repository

import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding
import com.google.cloud.speech.v1.{ RecognitionAudio, RecognitionConfig, SpeechClient, SpeechRecognitionResult }
import com.google.protobuf.ByteString
import com.xperiall.http.model.VoiceRecognitionRequest

import scala.collection.JavaConverters._
import scala.util.{ Failure, Success, Try }

object Repository {
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
