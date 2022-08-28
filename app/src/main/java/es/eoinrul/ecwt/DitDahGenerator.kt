package es.eoinrul.ecwt

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.view.KeyEvent
import androidx.preference.PreferenceManager
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.sin

enum class SoundTypes {
    DIT, DAH, LETTER_SPACE, WORD_SPACE
}

// Given a string, convert it to a sequence of SoundTypes representing the morse code
fun StringToSoundSequence(s : String) : List<SoundTypes> {
    if(s.isEmpty())
    {
        return listOf();
    }

    val first = when(s[0]) {
        ' ' -> listOf(SoundTypes.WORD_SPACE)
        'A' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        'B' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        'C' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        'D' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        'E' -> listOf(
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        'F' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        'G' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        'H' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        'I' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        'J' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        'K' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        'L' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        'M' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        'N' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        'O' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        'P' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        'Q' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        'R' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        'S' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        'T' -> listOf(
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        'U' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        'V' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        'W' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        'X' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        'Y' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        'Z' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        '0' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        '1' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        '2' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        '3' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        '4' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        '5' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        '6' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        '7' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        '8' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        '9' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        '.' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.LETTER_SPACE
        )
        '?' -> listOf(
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        '/' -> listOf(
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.DIT,
            SoundTypes.DAH,
            SoundTypes.DIT,
            SoundTypes.LETTER_SPACE
        )
        else -> { listOf() }
    }

    return first + StringToSoundSequence(s.substring(1));
}

// Given a list of SoundTypes, convert them to a string suitable for display
fun SequenceToString(sequence: List<SoundTypes>) : String {
    var ret = String()
    for(symbol in sequence) {
        ret += when(symbol) {
            SoundTypes.DIT -> "·"
            SoundTypes.DAH -> "-"
            else -> " "
        }
    }
    return ret
}

data class DitDahGeneratorSettings(var context : Context? = null) {
    //TODO These values are duplicated in the settings fragment
    var toneFrequency = 650
    var wordsPerMinute = 20
    var farnsworthWordsPerMinute = 20

    init {
        if(context != null) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            wordsPerMinute = sharedPreferences.getInt(context!!.getString(R.string.setting_wpm_key), wordsPerMinute)
            farnsworthWordsPerMinute = sharedPreferences.getInt(context!!.getString(R.string.setting_effective_wpm_key), farnsworthWordsPerMinute)

            // This one is a little strange; the ListPreference stores a string, which we need to parse
            toneFrequency = sharedPreferences.getString(context!!.getString(R.string.setting_tone_key), toneFrequency.toString())!!.toInt()
        }
    }
}

class DitDahSoundStream {
    interface StreamNotificationListener {
        // We ran out of symbols to add to the stream
        fun symbolsExhausted(stream : DitDahSoundStream)
    }

    constructor(config : DitDahGeneratorSettings) {
        // Farnsworth timing calculations: https://morsecode.world/international/timing.html

        val t_dit = 60.0f / (50.0f * config.wordsPerMinute)
        val t_fdit = ((60.0f / min(config.farnsworthWordsPerMinute, config.wordsPerMinute)) - 31.0f * t_dit) / 19.0f

        val ditLengthInSamples = (t_dit * mAudioSampleRate).toInt()
        val dahLengthInSamples = (t_dit * 3 * mAudioSampleRate).toInt()
        val intraCharacterSpacingInSamples = (t_dit * mAudioSampleRate).toInt()
        val interCharacterSpacingInSamples = (3 * t_fdit * mAudioSampleRate).toInt()
        val wordSpacingInSamples = (7 * t_fdit * mAudioSampleRate).toInt()

        mDitSound = ShortArray(ditLengthInSamples + intraCharacterSpacingInSamples)
        mDahSound = ShortArray(dahLengthInSamples + intraCharacterSpacingInSamples)

        // These two subtract the intra-character spacing, because they'll already have been played by the most recent sound
        mWordSpacingSound = ShortArray(wordSpacingInSamples - intraCharacterSpacingInSamples);
        mCharacterSpacingSound = ShortArray(interCharacterSpacingInSamples - intraCharacterSpacingInSamples);

        val invSampleRate = 1.0 / mAudioSampleRate.toFloat()
        for(i in 0 until ditLengthInSamples) {
            mDitSound[i] = (0x7fff.toFloat() * sin( 2.0f * PI * i * config.toneFrequency * invSampleRate)).toShort()
        }

        for(i in 0 until dahLengthInSamples) {
            mDahSound[i] = (0x7fff.toFloat() * sin(2.0f * PI * i * config.toneFrequency * invSampleRate) ).toShort()
        }

        // Apply an attack and release envelope to each of the samples to avoid a pop
        // in the audio that's generated. Empirically, 8ms seems to eliminate it while
        // still being fast enough that it works on the fastest WPM setting:
        val attackReleaseDuration = 0.008f;
        val attackReleaseTimeInSamples = (attackReleaseDuration * mAudioSampleRate).toInt();
        for(i in 0 until attackReleaseTimeInSamples) {
            val frac = i.toFloat() / attackReleaseTimeInSamples.toFloat();
            mDitSound[i] = (frac * mDitSound[i]).toShort()
            mDahSound[i] = (frac * mDahSound[i]).toShort()

            mDitSound[ditLengthInSamples - i - 1] = (frac * mDitSound[ditLengthInSamples - i - 1]).toShort();
            mDahSound[dahLengthInSamples - i - 1] = (frac * mDahSound[dahLengthInSamples - i - 1]).toShort();
        }

        // Create a thread that will pull symbols off our queue and write samples to the audio
        // device, so it doesn't matter if the write is blocked.
        thread() { makeSoundsWorkerThreadFunc() }
    }

    fun enqueue(symbols : List<SoundTypes>) {
        for(sym in symbols) {
            mSymbolQueue.put(sym)
        }
    }

    // Returns the duration of the sequence, in seconds
    fun durationOf(symbols : List<SoundTypes>): Float {
        var numSamples = 0
        for(sym in symbols) {
            numSamples += when(sym) {
                SoundTypes.DIT -> mDitSound
                SoundTypes.DAH -> mDahSound
                SoundTypes.LETTER_SPACE -> mCharacterSpacingSound
                SoundTypes.WORD_SPACE -> mWordSpacingSound
            }.size
        }

        return numSamples.toFloat() / mAudioSampleRate.toFloat();
    }

    fun quit() {
        mShouldQuit = true;

        // Pause and flush the audio player, which has the effect of stopping
        // playback immediately.
        mSoundPlayer.pause();
        mSoundPlayer.flush();

        // Add a symbol to the queue to ensure the worker thread is released to see the quit signal
        mSymbolQueue.put(SoundTypes.WORD_SPACE)
    }

    fun makeSoundsWorkerThreadFunc() {
        while(true) {
            // Try to get a symbol from the queue:
            var didWait : Boolean = false
            var sym : SoundTypes? = mSymbolQueue.poll(1, TimeUnit.MILLISECONDS)

            if(sym == null) {
                // We didn't get a symbol quickly enough; this happens during the
                // interactive sounder mode, where the user is pressing keys or
                // when we have hit the end of a sequence.
                // Tell any listener, give them the opportunity to restart or quit:
                streamNotificationListener?.symbolsExhausted(this)
                // Now we can wait indefinitely:
                didWait = true
                sym = mSymbolQueue.take()
            }

            if(mShouldQuit)
                return

            val soundToWrite : ShortArray = when (sym!!) {
                SoundTypes.DIT -> mDitSound
                SoundTypes.DAH -> mDahSound
                SoundTypes.LETTER_SPACE -> mCharacterSpacingSound
                SoundTypes.WORD_SPACE -> mWordSpacingSound
            }

            if (didWait) {
                // If we had to wait for a character, that means we are
                // in interactive mode. Sometimes, a pop can be heard at
                // the start, so add an extra bit of silence, to fill up
                // the audio buffers. If the buffer is smaller than this,
                // it should be OK, since we just want leading silence:
                mSoundPlayer.write(mCharacterSpacingSound, 0, mCharacterSpacingSound.size)
            }

            // Now attempt to write the real sound:
            var numWritten = mSoundPlayer.write(soundToWrite, 0, soundToWrite.size)
            // And start the audio playing:
            mSoundPlayer.play();

            if (numWritten != soundToWrite.size) {
                mSoundPlayer.write(
                    soundToWrite,
                    numWritten,
                    soundToWrite.size - numWritten,
                    AudioTrack.WRITE_BLOCKING
                )
            }

            // Now we can stop the audio player; it won't stop the audio
            // immediately, instead once the last write() has been output
            mSoundPlayer.stop()
        }
    }

    // Optional listener for stream finished event
    var streamNotificationListener : StreamNotificationListener? = null

    // Used to notify our audio-track-writing thread that it should quit
    private var mShouldQuit = false;

    // A queue of sounds we want to play
    private var mSymbolQueue = ArrayBlockingQueue<SoundTypes>(1000);

    private val mAudioSampleRate = 44100;

    // Buffers containing the sounds we want to play
    private val mDitSound : ShortArray;
    private val mDahSound : ShortArray;
    private val mWordSpacingSound : ShortArray;
    private val mCharacterSpacingSound : ShortArray;

    // Our actual audio track
    private val mSoundPlayer : AudioTrack = AudioTrack.Builder()
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        .setAudioFormat(
            AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(mAudioSampleRate)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build()
        )
        .setTransferMode(AudioTrack.MODE_STREAM)
        .setBufferSizeInBytes(mAudioSampleRate)
        .build();
}
