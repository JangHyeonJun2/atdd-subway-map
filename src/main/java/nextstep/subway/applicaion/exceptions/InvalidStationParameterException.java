package nextstep.subway.applicaion.exceptions;

import nextstep.subway.enums.exception.ErrorCode;

public class InvalidStationParameterException extends AbstractException{
    public InvalidStationParameterException(ErrorCode errorCode) {
        super(errorCode);
    }
}
