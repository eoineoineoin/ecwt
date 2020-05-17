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

        // Get the lesson number, for display in the UI
        fun indexForHumans() : Int {
            return lessonIndex + 1
        }

        fun getAlphabet() : String {
            return LessonOrder.substring(0, lessonIndex + 2)
        }
    }
}