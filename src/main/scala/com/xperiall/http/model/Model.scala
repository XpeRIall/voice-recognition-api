/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.xperiall.http.model

import com.google.cloud.speech.v1.SpeechRecognitionResult
import org.joda.time.DateTime

final case class Model(
    vString: String,
    vInt: Int,
    vLong: Long,
    vFloat: Float,
    vDouble: Double,
    vSeqInt: Seq[Int],
    vListInt: List[Int])

final case class VoiceRecognitionRequest(userToken: String, data: Array[Byte])

final case class VoiceRecognitionResponse(userToken: String, transcriptions: Seq[SpeechRecognitionResult], createdAt: DateTime)