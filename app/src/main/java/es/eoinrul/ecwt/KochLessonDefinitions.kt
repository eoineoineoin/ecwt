package es.eoinrul.ecwt

object KochLessonDefinitions {
    private const val LessonOrder = "KMRSUAPTLOWI.NJEF0YVG5/Q9ZH38B?427C1D6X" // This order is from http://www.hfradio.org/koch_2.html //TODO Add prosign lessons?

    val Lessons : MutableList<KochLesson> = ArrayList()

    init {
        for (i in 0 until LessonOrder.length - 1) {
            Lessons.add(
                KochLesson(
                    i
                )
            )
        }
    }

    data class KochLesson(val lessonIndex : Int) {
        override fun toString() : String {
            if(lessonIndex == 0) {
                // The first lesson is a little special, as it introduces two letters:
                return LessonOrder.substring(0, 1) + " and " + LessonOrder.substring(1, 2)
            }
            return LessonOrder.substring(lessonIndex + 1 , lessonIndex + 2)
        }

        // Return a string containing the new character introduced in this lesson
        // The first lesson contains two characters. Lesson indices created past the end of the
        // defined set of characters will return an empty string
        fun newCharacters() : String {
            try {
                if(lessonIndex == 0) {
                    return LessonOrder.substring(0, 1) + " " + LessonOrder.substring(1, 2)
                } else {
                    return LessonOrder.substring(lessonIndex + 1, lessonIndex + 2)
                }
            } catch (e : StringIndexOutOfBoundsException) {
                return ""
            }
        }

        // Return a string containing the dots and dashes for the characters
        // introduced in this lesson
        fun newSignsAsString() : String {
            return SequenceToString(StringToSoundSequence(newCharacters()))
        }

        // Get the lesson number, for display in the UI
        fun indexForHumans() : Int {
            return lessonIndex + 1
        }

        fun getAlphabet() : String {
            return LessonOrder.substring(0, lessonIndex + 2)
        }
    }
}