package unit;

import com.usuario.todolist.dto.request.TaskFilterRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class TaskFilterRequestTest {

    @Test
    void constructor_WithNullDescription_RemainsNull() {
        TaskFilterRequest filter = new TaskFilterRequest(null, (LocalDateTime) null, null, null);

        assertThat(filter.description()).isNull();
    }

    @Test
    void constructor_WithBlankDescription_StripsToEmpty() {
        TaskFilterRequest filter = new TaskFilterRequest("   ", (LocalDateTime) null, null, null);

        assertThat(filter.description()).isEmpty();
    }

    @Test
    void constructor_WithWhitespace_NormalizesSpaces() {
        TaskFilterRequest filter = new TaskFilterRequest("  Tarea   importante  ",
                (LocalDateTime) null, null, null);

        assertThat(filter.description()).isEqualTo("Tarea importante");
    }

    @Test
    void constructor_WithLocalDates_ConvertsToStartAndEndOfDay() {
        LocalDate minDate = LocalDate.of(2024, 1, 1);
        LocalDate maxDate = LocalDate.of(2024, 12, 31);

        TaskFilterRequest filter = new TaskFilterRequest("test", minDate, maxDate, true);

        assertThat(filter.minDate()).isEqualTo(minDate.atStartOfDay());
        assertThat(filter.maxDate()).isEqualTo(maxDate.atTime(LocalTime.MAX));
    }

    @Test
    void constructor_WithNullDates_RemainsNull() {
        TaskFilterRequest filter = new TaskFilterRequest("test", (LocalDate) null, null, null);

        assertThat(filter.minDate()).isNull();
        assertThat(filter.maxDate()).isNull();
    }
}