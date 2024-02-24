package ru.practicum.shareit.booking.enums;

/**
 * BookingStatus — необязательный параметр запроса эндпойнта: GET /bookings?state={state}.
 * Может принимать одно из следующих значений:
 * ALL (англ. «все») — значение по умолчанию,
 * CURRENT (англ. «текущие»),
 * PAST (англ. «завершённые»),
 * FUTURE (англ. «будущие»),
 * WAITING (англ. «ожидающие подтверждения»),
 * REJECTED (англ. «отклонённые»).
 */
public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED
}

