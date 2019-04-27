package Interface;

import bardiademon.Interface.bardiademon;

@bardiademon
public interface ResultModel
{

    // MAX NUMBER => 16

    @bardiademon
    abstract class ResultCheckRequest extends PublicResult
    {
        public static final int ERROR = -1;
    }

    @bardiademon
    abstract class CheckSerialNumber extends PublicResult
    {
        public static final int ACTIVE_ACCOUNT = 2, DEACTIVE_ACCOUNT = 3, NOT_FOUND_SERIAL = 4;
    }

    @bardiademon
    abstract class PublicResult
    {
        public static final int PUBLIC_ERROR_REQUEST = -11;
        public static final int PUBLIC_ERROR = -12;
        public static final int NOT_LOGGED_IN = -13;
        public static final int NOT_FOUND = 0;
        public static final int IS_OK = 7;
        public static final int NOT_OK = -7;
        public static final int FOUND = 1;
        public static final int NULL = -123;

        // For Log Server
        public static final int SERVER = 471;

        @bardiademon
        public static abstract class ForLog
        {
            public static final String SHOW_RESULT_TO_CLIENT = "show_result_to_client";
            public static final int ID_NOT_FOUND = -14;
        }
    }

    @bardiademon
    abstract class DelMemoirMemoir extends PublicResult
    {
        // SC = Status Code
        public static abstract class SC200
        {
            public static final int DELETED = 13;
            public static final int NOT_DELETED = 14;
        }
    }

    @bardiademon
    abstract class CheckInfoLogin extends CheckSerialNumber
    {
        public static final int INVALID = 5, VALID = 6;
    }

    @bardiademon
    abstract class RecordNewMemoir extends PublicResult
    {
        // SC = Status Code
        public static abstract class SC400
        {
            public static final int DUPLICATE_LINK = 12;
        }

        public static abstract class SC200
        {
            public static final int NOT_RECORDED = 10, RECORDED = 11;
            public static final int ERROR_LINK = 8, ERROR_DATE = 9;
        }

    }

    @bardiademon
    abstract class GetMemoirUser extends PublicResult
    {
        // SC = Status Code
        public static abstract class SC200 extends GetMemoirUser
        {
        }
    }

    @bardiademon
    abstract class GetMemoir extends PublicResult
    {
        // SC = Status Code
        public static abstract class SC200 extends GetMemoirUser
        {
            public static final int NOT_OPEN = 15, NOT_CONFIRMATION = 16;
        }
    }

    // KJR => Key Json Response
    interface KJR
    {
        abstract class KJRPublic
        {
            public static final String NAME = "name", ID = "id";
        }

        abstract class KJRGetMemoirUser extends KJRPublic
        {
            public static final String SUBJECT = "sub", CONFIRMATION = "con", LINK = "lnk", OPEN = "opn";
        }

        abstract class KJRGetMemoir extends KJRPublic
        {
            public static final String
                    NAME = "name",
                    SUBJECT = "sbjct",
                    TEXT = "txt",
                    DATE = "date",
                    TIME_RECORD = "timrcrd",
                    TIME_LAST_EDIT = "timlstedt",
                    VISIT = "vst",
                    VISIT_USER = "vstusr",
                    LIKE = "lk",
                    COMMENT = "cmt",
                    LIKED = "lkd",
                    IS_MEMOIR_FOR_YOU = "imfy";

        }

        abstract class KJRSubject extends KJRPublic
        {
            public static final String NAME = "nm";
        }

        abstract class KJRGetComment extends KJRPublic
        {
            public static final String
                    TXT_COMMENT = "tcmnt",
                    TIME = "tm",
                    USERNAME = "uname",
                    PIC = "pic";

        }
    }
}
