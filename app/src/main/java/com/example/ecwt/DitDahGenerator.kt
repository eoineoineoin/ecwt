package com.example.ecwt

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaDataSource
import android.view.KeyEvent
import java.util.concurrent.ArrayBlockingQueue
import kotlin.concurrent.thread
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.sin

enum class SoundTypes {
    DIT, DAH, LETTER_SPACE, WORD_SPACE
}

fun StringToSoundSequence(s : String) : List<SoundTypes> {
    if(s.isEmpty())
    {
        return listOf();
    }

    val first = when(s[0]) {
        ' ' -> listOf(SoundTypes.WORD_SPACE)
        'A' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        'B' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        'C' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        'D' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        'E' -> listOf(SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        'F' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        'G' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        'H' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        'I' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        'J' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        'K' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        'L' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        'M' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        'N' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        'O' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        'P' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        'Q' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        'R' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        'S' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        'T' -> listOf(SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        'U' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        'V' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        'W' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        'X' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        'Y' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        'Z' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        '0' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        '1' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        '2' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        '3' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        '4' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        '5' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        '6' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        '7' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        '8' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        '9' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        '.' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.LETTER_SPACE)
        '?' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.LETTER_SPACE)
        else -> { listOf() }
    }

    return first + StringToSoundSequence(s.substring(1));
}

// Almost certainly the wrong way to do this
fun KeycodeToSoundSequence(keycode : Int) : List<SoundTypes> {
    return when(keycode) {
        KeyEvent.KEYCODE_A -> StringToSoundSequence("A");
        KeyEvent.KEYCODE_B -> StringToSoundSequence("B");
        KeyEvent.KEYCODE_C -> StringToSoundSequence("C");
        KeyEvent.KEYCODE_D -> StringToSoundSequence("D");
        KeyEvent.KEYCODE_E -> StringToSoundSequence("E");
        KeyEvent.KEYCODE_F -> StringToSoundSequence("F");
        KeyEvent.KEYCODE_G -> StringToSoundSequence("G");
        KeyEvent.KEYCODE_H -> StringToSoundSequence("H");
        KeyEvent.KEYCODE_I -> StringToSoundSequence("I");
        KeyEvent.KEYCODE_J -> StringToSoundSequence("J");
        KeyEvent.KEYCODE_K -> StringToSoundSequence("K");
        KeyEvent.KEYCODE_L -> StringToSoundSequence("L");
        KeyEvent.KEYCODE_M -> StringToSoundSequence("M");
        KeyEvent.KEYCODE_N -> StringToSoundSequence("N");
        KeyEvent.KEYCODE_O -> StringToSoundSequence("O");
        KeyEvent.KEYCODE_P -> StringToSoundSequence("P");
        KeyEvent.KEYCODE_Q -> StringToSoundSequence("Q");
        KeyEvent.KEYCODE_R -> StringToSoundSequence("R");
        KeyEvent.KEYCODE_S -> StringToSoundSequence("S");
        KeyEvent.KEYCODE_T -> StringToSoundSequence("T");
        KeyEvent.KEYCODE_U -> StringToSoundSequence("U");
        KeyEvent.KEYCODE_V -> StringToSoundSequence("V");
        KeyEvent.KEYCODE_W -> StringToSoundSequence("W");
        KeyEvent.KEYCODE_X -> StringToSoundSequence("X");
        KeyEvent.KEYCODE_Y -> StringToSoundSequence("Y");
        KeyEvent.KEYCODE_Z -> StringToSoundSequence("Z");
        KeyEvent.KEYCODE_0 -> StringToSoundSequence("0");
        KeyEvent.KEYCODE_1 -> StringToSoundSequence("1");
        KeyEvent.KEYCODE_2 -> StringToSoundSequence("2");
        KeyEvent.KEYCODE_3 -> StringToSoundSequence("3");
        KeyEvent.KEYCODE_4 -> StringToSoundSequence("4");
        KeyEvent.KEYCODE_5 -> StringToSoundSequence("5");
        KeyEvent.KEYCODE_6 -> StringToSoundSequence("6");
        KeyEvent.KEYCODE_7 -> StringToSoundSequence("7");
        KeyEvent.KEYCODE_8 -> StringToSoundSequence("8");
        KeyEvent.KEYCODE_9 -> StringToSoundSequence("9");
        KeyEvent.KEYCODE_PERIOD -> StringToSoundSequence(".");
        //KeyEvent.KEYCODE_S -> StringToSoundSequence("?");
        else -> { StringToSoundSequence(" ")}
    }

}

class DitDahGeneratorSettings
{
    //TODO These values are duplicated in the settings fragment
    var toneFrequency = 650
    var wordsPerMinute = 20
    var farnsworthWordsPerMinute = 20
}

class DitDahSoundStream {
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

        //TODO: Ramp up? Make sound nicer? Seems to have clipping - just in the emulator?
        val invSampleRate = 1.0 / mAudioSampleRate.toFloat()
        for(i in 0 until ditLengthInSamples) {
            mDitSound[i] = (0x7fff.toFloat() * sin( 2.0f * PI * i * config.toneFrequency * invSampleRate)).toShort()
        }

        for(i in 0 until dahLengthInSamples) {
            mDahSound[i] = (0x7fff.toFloat() * sin(2.0f * PI * i * config.toneFrequency * invSampleRate) ).toShort()
        }

        thread() { makeSoundsWorkerThreadFunc() }
    }

    fun enqueue(symbols : List<SoundTypes>) {
        for(sym in symbols) {
            mSymbolQueue.put(sym)
        }
    }

    fun quit() {
        mShouldQuit = true;
        mSymbolQueue.put(SoundTypes.WORD_SPACE)
    }

    fun makeSoundsWorkerThreadFunc() {
        while(true) {
            val sym : SoundTypes = mSymbolQueue.take()
            if(mShouldQuit)
                return;

            when (sym) {
                SoundTypes.DIT -> mSoundPlayer.write(mDitSound, 0, mDitSound.size)
                SoundTypes.DAH -> mSoundPlayer.write(mDahSound, 0, mDahSound.size)
                SoundTypes.LETTER_SPACE -> mSoundPlayer.write( mCharacterSpacingSound, 0, mCharacterSpacingSound.size)
                SoundTypes.WORD_SPACE -> mSoundPlayer.write( mWordSpacingSound, 0, mWordSpacingSound.size)
            }

            if (mSoundPlayer.playState != AudioTrack.PLAYSTATE_PLAYING)
                mSoundPlayer.play()
        }
    }

    private var mShouldQuit = false;
    private var mSymbolQueue = ArrayBlockingQueue<SoundTypes>(1000);

    private val mAudioSampleRate = 44100;

    private val mDitSound : ShortArray;
    private val mDahSound : ShortArray;
    private val mWordSpacingSound : ShortArray;
    private val mCharacterSpacingSound : ShortArray;

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
        .setBufferSizeInBytes(mAudioSampleRate)
        .build();
}