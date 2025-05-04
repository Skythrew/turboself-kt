import kotlinx.datetime.*

data class WeekRange(val from: LocalDateTime, val to: LocalDateTime)

fun getWeekRange(weekNumber: Int, year: Int): WeekRange {
    val adjustedWeek = weekNumber - 2

    val firstDayOfYear = LocalDate(year, 1, 1)
    val dayOfWeek = firstDayOfYear.dayOfWeek

    val daysToFirstMonday = 8 - dayOfWeek.isoDayNumber

    val firstMonday = firstDayOfYear.plus(daysToFirstMonday, DateTimeUnit.DAY)

    val weekStartDate = firstMonday.plus(adjustedWeek * 7 + 1, DateTimeUnit.DAY)
    val weekStartDateTime = weekStartDate.atTime(0, 0)
    val weekEndDateTime = weekStartDate.plus(5, DateTimeUnit.DAY).atTime(23, 59, 59, 999_999_999)

    return WeekRange(weekStartDateTime, weekEndDateTime)
}
