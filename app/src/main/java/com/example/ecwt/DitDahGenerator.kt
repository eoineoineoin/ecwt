package com.example.ecwt

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaDataSource
import android.view.KeyEvent
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import kotlin.concurrent.thread
import kotlin.math.PI
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
        'A' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.WORD_SPACE) 
        'B' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.WORD_SPACE) 
        'C' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        'D' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        'E' -> listOf(SoundTypes.DIT, SoundTypes.WORD_SPACE)
        'F' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        'G' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        'H' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        'I' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        'J' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        'K' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        'L' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        'M' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        'N' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        'O' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        'P' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        'Q' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        'R' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        'S' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        'T' -> listOf(SoundTypes.DAH, SoundTypes.WORD_SPACE)
        'U' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        'V' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        'W' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        'X' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        'Y' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        'Z' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        '0' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        '1' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        '2' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        '3' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        '4' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        '5' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        '6' -> listOf(SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        '7' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        '8' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        '9' -> listOf(SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.WORD_SPACE)
        '.' -> listOf(SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.WORD_SPACE)
        '?' -> listOf(SoundTypes.DIT, SoundTypes.DIT, SoundTypes.DAH, SoundTypes.DAH, SoundTypes.DIT, SoundTypes.DIT, SoundTypes.WORD_SPACE)
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


class DitDahGenerator : MediaDataSource {
    constructor() : super()

    override fun close() {
    }
    override fun getSize() : Long {
        return 80000;
    }

    override fun readAt(position: Long, buffer: ByteArray?, offset: Int, size: Int): Int {
        if(position > 10000000)
            return -1;
/*
        for(i in 0..size) {
            val xt = sin((i + offset) / 44e3f) * 255.0f;
            buffer?.set(i + offset, xt.toByte());
        }
*/
        return size;
    }

}

class DitDahGeneratorSettings
{
    var toneFrequency = 650 //TODO These values are duplicated in the settings fragment
    var WordsPerMinute = 20;
}

class DitDahSoundStream {
    constructor(config : DitDahGeneratorSettings) {
        // Farnsworth timing calculations: https://morsecode.world/international/timing.html
        //TODO: Compute these properly
        val ditLengthSeconds = 0.15f;
        val dahLengthSeconds = ditLengthSeconds * 3.0f;

        val ditLengthInSamples = (ditLengthSeconds * mAudioSampleRate).toInt();
        val dahLengthInSamples = (dahLengthSeconds * mAudioSampleRate).toInt();
        val interCharacterSpacingInSamples = ditLengthInSamples;

        mDitSound = ShortArray(ditLengthInSamples + interCharacterSpacingInSamples);
        mDahSound = ShortArray(dahLengthInSamples + interCharacterSpacingInSamples);
        mWordSpacingSound = ShortArray((dahLengthSeconds * mAudioSampleRate).toInt());
        mCharacterSpacingSound = ShortArray((ditLengthSeconds * mAudioSampleRate).toInt());

        //TODO: Ramp up? Make sound nicer? Seems to have clipping - just in the emulator?
        val invSampleRate = 1.0 / mAudioSampleRate.toFloat()
        for(i in 0 until ditLengthInSamples) {
            mDitSound[i] = (0x7fff.toFloat() * sin( 2.0f * PI * i * config.toneFrequency * invSampleRate)).toShort();
        }

        for(i in 0 until dahLengthInSamples) {
            mDahSound[i] = (0x7fff.toFloat() * sin(2.0f * PI * i * config.toneFrequency * invSampleRate) ).toShort();
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