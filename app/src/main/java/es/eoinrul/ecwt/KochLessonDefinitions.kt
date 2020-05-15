package es.eoinrul.ecwt

object KochLessonDefinitions {
    private const val LessonOrder = "KMRSUAPTLOWI.NJEF0YVG5/Q9ZH38B?427C1D6X" // This order is from http://www.hfradio.org/koch_2.html //TODO Add prosign lessons?

    val Lessons : MutableList<KochLesson> = ArrayList()

    init {
        for (i in 1 until LessonOrder.length) {
            Lessons.add(
                KochLesson(
                    i
                )
            )
        }
    }

    data class KochLesson(val lessonIndex : Int) {
        override fun toString() : String {
            // The zeroth lesson doesn't exist, since it would only contain a single character //TODO raise exception?

            if(lessonIndex == 1) {
                // The first lesson is a little special, as it introduces two letters:
                return LessonOrder.substring(0, 1) + " and " + LessonOrder.substring(1, 2)
            }
            return LessonOrder.substring(lessonIndex , lessonIndex + 1)
        }

        fun indexForHumans() : Int {
            return lessonIndex
        }

        fun getAlphabet() : String {
            return LessonOrder.substring(0, lessonIndex + 1)
        }
    }
}