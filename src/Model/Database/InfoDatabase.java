package Model.Database;

import bardiademon.Interface.bardiademon;

public interface InfoDatabase
{
    @bardiademon
    abstract class PublicRow
    {
        public static final String ID = "id", NAME = "name", STATUS = "status", ID_USER = "id_user";

        public static abstract class DV
        {
            public static final int IS_ACCEPT = 1,
                    IS_NOT_ACCEPT = 0,
                    STATUS_ACTIVE = 1;
        }
    }

    @bardiademon
    abstract class TRequest extends PublicRow
    {
        public static final String NT = "request", NR_ID = "id", NR_REQUEST = "request", NR_PAGE_CODE = "page_code";
    }

    abstract class TAccount extends PublicRow
    {
        public static final String NT = "account";
        public static final String
                USERNAME = "username",
                PASSWORD = "password",
                SERIAL = "serial",
                NAME_PIC = "name_pic",
                TIME_REGISTER = "time_register",
                ABOUT = "about";

        public static abstract class DV extends PublicRow.DV
        {
            public static final String NAME_PIC__NO_PIC = "no_pic";
        }

    }

    abstract class TSubject extends PublicRow
    {
        public static final String NT = "sub";
    }

    abstract class TMemoirList extends PublicRow
    {
        // id_user , name => PublicRow

        public static final String NT = "memoir_list";
        public static final String
                SUBJECT = "subject",
                DATE = "date",
                OPEN = "open",
                LINK = "link",
                VISIT = "visit",
                VISIT_USER = "visit_user",
                TEXT = "text",
                TIME_RECORD = "time_record",
                TIME_LAST_EDIT = "time_last_edit",
                CONFIRMATION = "confirmation",
                NUMBER_OF_LIKE = "numberof_like",
                NUMBER_OF_COMMENT = "numberof_comment";

        public static abstract class DV extends PublicRow.DV
        {
            public static final int IS_OPEN_OR_CONFIRMATION = 1;
        }
    }

    abstract class TLike extends PublicRow
    {
        // ID
        public static final String NT = "like";
        public static final String ID_MEMOIR = "id_memoir", ID_USER = "id_user";
    }

    abstract class TComment extends TLike
    {
        // ID
        public static final String NT = "comment";
        public static final String TXT_COMMENT = "comment";
        public static final String TIME = "time";
    }

    abstract class TVisitUser extends TLike
    {
        // ID
        public static final String NT = "visit_user";
    }

}
