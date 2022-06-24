package com.example.combinedproject.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;
import android.widget.Toast;

import com.mapbox.mapboxsdk.geometry.LatLng;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NavigationBIUDB";
    private static final String MARKERS_TABLE_NAME = "markers";
    private static final String USERS_TABLE_NAME = "users";
    private static final String FAVORITES_TABLE_NAME = "favorites";

    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_LOCATION_LAT = "location_lat";
    private static final String KEY_LOCATION_LON = "location_lon";
    private static final String KEY_DESCRIPTION = "description";

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_CONNECTION_TYPE = "connection_type";
   /* private SQLiteDatabase writableDB;
    private SQLiteDatabase raedableDB;*/


    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MARKERS_TABLE = "CREATE TABLE " + MARKERS_TABLE_NAME + "("
                + KEY_NAME + " TEXT PRIMARY KEY," + KEY_TYPE + " TEXT,"
                + KEY_LOCATION_LAT + " REAL," + KEY_LOCATION_LON + " REAL," + KEY_DESCRIPTION + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_MARKERS_TABLE);

        String CREATE_USERS_TABLE = "CREATE TABLE " + USERS_TABLE_NAME + "("
                + KEY_USERNAME + " TEXT PRIMARY KEY," + KEY_PASSWORD + " TEXT,"
                + KEY_NAME + " TEXT," + KEY_USER_TYPE + " TEXT," + KEY_CONNECTION_TYPE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);

        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + FAVORITES_TABLE_NAME + "("
                + KEY_USERNAME + " TEXT," + KEY_NAME + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_FAVORITES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MARKERS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FAVORITES_TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public void addAllMarkers() {
        //amphitheaters
        insertData("1501 - אמפי-תיאטרון מרכזי", "amphitheater", 32.07265, 34.848452, "האמפי-תיאטרון המרכזי בבר אילן, ממוקם בחלק הצפוני של הקמפוס. משמש לטקסים, הופעות, אירועים שונים ועוד.");
        insertData("110 - אמפי-תיאטרון דרומי", "amphitheater", 32.066434, 34.842545, "האמפי-תיאטרון הדרומי בבר אילן,  משמש לטקסים, הופעות, אירועים שונים ועוד.");
        insertData("בניין 1401 - מרכז וואהל", "amphitheater", 32.071615, 34.846645, "מרכז כנסים ואירועים");

//dorms
        insertData("בניין 106 - מעונות סודנטים על שם מוסקוביץ' - פרשין", "dorms", 32.065939, 34.84213, "מעונות סטודנטים, בחלק הדרומי של הקמפוס");
        insertData("בניין 108 - מעונות סטודנטים על שם סטולמן", "dorms", 32.065964, 34.843286, "מעונות סטודנטים, בחלק הדרומי של הקמפוס");
        insertData("בניין 506 - מעונות סטודנטים על שם גרוז / אלברט הוברט", "dorms", 32.070765, 34.844941, "מעונות סטודנטים");
        insertData("בניין 104 - מעונות סטודנטים על שם א' וולפסון", "dorms", 32.066241, 34.840757, "מעונות סטודנטים, בחלק הדרומי של הקמפוס");
        insertData("בניין 1300 B - מעונות סודנטים", "dorms", 32.071051, 34.850034, "מעונות סטודנטים, החדשים ביותר בקמפוס. ניתן למצוא באיזור הבניין מסעדות, חנויות ועוד.");
        insertData("בניין 1300 A - מעונות סודנטים", "dorms", 32.072127, 34.850121, "מעונות סטודנטים, החדשים ביותר בקמפוס. ניתן למצוא באיזור הבניין מסעדות, חנויות ועוד.");
        insertData("בניין 103 - מעונות סטודנטים על שם נסים ד' גאון", "dorms", 32.066574, 34.840247, "מעונות סטודנטים, בחלק הדרומי של הקמפוס");
        insertData("בניין 101 - מעונות סטודנטים על שם שרמן", "dorms", 32.065994, 34.840105, "מעונות סטודנטים, בחלק הדרומי של הקמפוס");
        insertData("בניין 802 - מעון יום גריידל וויספלד", "building", 32.071987, 34.844709, "מעון יום");

//buildings
        insertData("בניין 403 - בית הספר לחינוך א' על שם צ'רלס גרוסברג", "building", 32.068406, 34.843197, "בניין לימודים");
        insertData("בניין 105 - כיתות אקסודוס על שם רוברט אסרף", "building", 32.066002, 34.841735, "בניין לימודים");
        insertData("בניין 212 - מדעי החי על שם סוויסה", "building", 32.067667, 34.841833, "בניין לימודים");
        insertData("בניין 902 - בנין פסיכולוגיה על שם אנה ומקס ווב ומשפחתם", "building", 32.072892, 34.846444, "בניין לימודים");
        insertData("בניין 409 - בית הסגל על שם סלומון קרוק", "building", 32.06925, 34.842877, "בניין לימודים");
        insertData("בניין 403 - בית הספר לחינוך א' על שם צ'רלס גרוסברג", "building", 32.068406, 34.843197, "בניין לימודים");
        insertData("בניין 206 - הטריפלקס לננוטכנולוגיה על שם לסלי וסוזן גונדה(גולדשמיד)", "building", 32.067002, 34.841356, "בניין לימודים");
        insertData("בניין 503 - בניין מדעי המחשב על שם אלכסנדר גראס - בבניה", "building", 32.069847, 34.843723, "בניין לימודים");
        insertData("בניין 1005 - בניין מוסיקה על שם מרקוס ואן רוזנברג", "building", 32.074027, 34.847685, "בניין לימודים");
        insertData("בניין 1004 - בניין לאלקטרוניקה-פיזיקלית על שם אהרון ורחל דהאן", "building", 32.073796, 34.847463, "בניין לימודים");
        insertData("בניין 905 - בניין בית הספר לחינוך על שם שמעון בן-יוסף", "building", 32.073128, 34.845849, "בניין לימודים");
        insertData("בניין 407 - המרכז לייעוץ לסטודנטים על שם מריה פינקל ורות רקמן", "building", 32.068652, 34.844821, "בניין לימודים");
        insertData("בניין 102 - מעבדות למיקרוביולוגיה על שם רפפורט", "building", 32.065873, 34.840769, "בניין לימודים");
        insertData("בניין 205 - המרכז למדעי החי על שם קונין-לוננפלד", "building", 32.066991, 34.840719, "בניין לימודים");
        insertData("בניין 109 - בית לאוטרמן", "building", 32.066343, 34.841641, "בניין לימודים");
        insertData("בניין 508 - משרדים", "building", 32.071426, 34.843739, "בניין משרדים");
        insertData("בניין 507 - בית מורשת ישראל על שם משפחת יעקב ופייגא נגל", "building", 32.071102, 34.844597, "בניין לימודים");
        insertData("בניין 306 - האגף למשפט מקרקעין על שם ז'אן ומוריס בנין", "building", 32.067674, 34.844351, "בניין לימודים");
        insertData("בניין 408 - בית הספר לחינוך ב' על שם אברהם קרוק", "building", 32.068961, 34.842698, "בניין לימודים");
        insertData("בניין 207 - כימיה על שם הרי קרפל", "building", 32.066953, 34.842135, "בניין לימודים");
        insertData("בניין 505 - כיתות אקסודוס על שם אהרון ורחל דהאן", "building", 32.070431, 34.844554, "בניין לימודים");
        insertData("בניין 307 - המרכז למבקרים ומצפה לב הקמפוס על שם ונגרובסקי", "building",  32.067315, 34.843013, "בניין לימודים");
        insertData("בניין 107 - בית הסטודנט על שם ג'רום סיסלמן", "building", 32.065751, 34.842739, "בניין לימודים");
        insertData("בניין 1105 - בנין לביו-הנדסה על שם מרק ריץ'", "building", 32.072904, 34.84945, "בניין לימודים");
        insertData("בניין 100 - בית החיות", "building", 32.065504, 34.840805, "בניין לימודים");
        insertData("בניין 504 - כלכלה ומינהל עסקים על שם דניאל ש' אברהם", "building", 32.069743, 34.844346, "בניין לימודים");
        insertData("בניין 404 - מדעי הרוח על שם צ'רלס וולפסון", "building", 32.068736, 34.843213, "בניין לימודים");
        insertData("בניין 402 - מנהלה על שם סטולמן", "building", 32.068055, 34.843208, "בניין לימודים");
        insertData("בניין 214 - מרכז לגילוי מוקדם של הסרטן על שם ג'רום שוטנשטיין", "building", 32.067654, 34.840838, "בניין לימודים");
        insertData("בניין 301 - המרכז הבינלאומי לקונגרסים על שם שמשון וחנה פלדמן", "building", 32.06644, 34.843596, "בניין לימודים");
        insertData("בניין 208 - המרכז לחקר הסרטן על שם מרילין פינקלר", "building", 32.067192, 34.840768, "בניין לימודים");
        insertData("בניין 217 - בית עליזה ומנחם בגין", "building", 32.069133, 34.842153, "בניין לימודים");
        insertData("בניין 304 - אולמות הרצאה על שם פולק", "building", 32.066904, 34.843178, "בניין לימודים");
        insertData("בניין 110 - הפארק על שם בוב שאפל", "building", 32.066418, 34.842629, "בניין לימודים");
        insertData("בניין 1002 - בית הרב יעקובוביץ - מרכז סמי שמעון ללימודי פילוסופיה, אתיקה ומחשבת ישראל", "building", 32.073798, 34.846834, "בניין לימודים");
        insertData("בניין 605 - הבנין ללימודי תארים מתקדמים על שם משפחת ג'רום ל. שטרן", "building", 32.070335, 34.843566, "בניין לימודים");
        insertData("בניין 215 - המרכז לאנרגיה על שם מותק ומרים קינדרלרר", "building", 32.067886, 34.841082, "בניין לימודים");
        insertData("בניין 300 - מתחם על שם איזידור ואידה לוקר", "building", 32.066384, 34.843866, "בניין לימודים");
        insertData("בניין 201 - ביולוגיה סביבתית ופוריות הגבר על שם חיים ורוז פריימן", "building", 32.066563, 34.840927, "בניין לימודים");
        insertData("בניין 604 - הבנין ללימודים בין-תחומיים על שם מרדכי ומוניק כץ", "building", 32.070281, 34.843865, "בניין לימודים");
        insertData("בניין 1104 - בנין לאלקטרוניקה-פיזיקלית על שם אהרון ורחל דהאן", "building", 32.073056, 34.849276, "בניין לימודים");
        insertData("בניין 1102 - בנין להנדסת מחשבים על שם מינה ואווררד גודמן", "building", 32.073383, 34.848898, "בניין לימודים");
        insertData("בניין 302 - לוגיסטיקה", "building", 32.066419, 34.844323, "בניין לוגיסטיקה");
        insertData("בניין 204 - המרכז למחקר רפואי-אבחוני על שם גונדה(גולדשמיד)", "building", 32.066882, 34.840702, "בניין לימודים");
        insertData("בניין 410 - מדעי היהדות על שם ברוך ורות רפפורט", "building", 32.069102, 34.843655, "בניין לימודים");
        insertData("בניין 210 - מרכז מבקרים - מוזיאון פטר לאמנות ולמדעי הננו", "building", 32.067296, 34.84267, "בניין לימודים");
        insertData("בניין 502 - כיתות ומשרדים", "building", 32.070714, 34.843245, "בנייני כיתות ומשרדים");
        insertData("בניין 405 - המדרשה", "building", 32.068455, 34.843695, "המדרשה");
        insertData("בניין 510 - אגודת הסטודנטים - הקוביה המרכזית", "building", 32.071813, 34.844011, "בניין לימודים");
        insertData("בניין 202 - פיסיקה-המכון לחקר המוצק", "building", 32.06664, 34.841625, "בניין לימודים");
        insertData("בניין 509 - לשכת דיקן הסטודנטים, לשכת רב הקמפוס", "building", 32.072009, 34.844072, "בניין לימודים");
        insertData("בניין 305 - משפטים על שם ארל וג'ני לון", "building", 32.06699, 34.844345, "בניין לימודים");
        insertData("בניין 211 - כימיה על שם בן ובלה לכטר", "building", 32.067364, 34.841954, "בניין לימודים");
        insertData("בניין 411 - המכון הגבוה לתורה על שם לודביג ואריקה יסלזון", "building", 32.06911, 34.84445, "בניין לימודים");
        insertData("בניין 303 - אחזקה", "building", 32.066763, 34.844313, "בניין אחזקה");
        insertData("בניין 1103 - בנין לטכנולוגיות מידע על שם דוקטור מרדכי ודוקטור מוניק כץ", "building", 32.073235, 34.849082, "בניין לימודים");
                insertData("בניין 203 - פיסיקה על שם שטרן", "building", 32.066677, 34.842137, "בניין לימודים");
        insertData("בניין 213 - מדעי החברה על שם קהילת מקסיקו", "building", 32.067989, 34.841846, "בניין לימודים");
        insertData("בניין 216 - מרכז המחשבים והמתמטיקה על שם עוזיאל שפיגל-יד עוזיאל", "building", 32.068614, 34.842078, "בניין לימודים");
        insertData("בניין 209 - המכון לטכנולוגיה מתקדמת על שם ג'ק ופרל רזניק", "building", 32.067432, 34.84083, "בניין לימודים");
        insertData("בניין 901 - המרכז לחקר המוח על שם לסלי וסוזאן גונדה(גולדשמיד) ", "building", 32.072673, 34.846039, "בניין לימודים");

//libraries
        insertData("ספרייה למדעי החיים", "library", 32.067727, 34.841814, "ממוקמת בבניין 212 בקומה 1, חדר 113");
        insertData("ספריית משפטים", "library", 32.066976, 34.844388, "ממוקמת בבניין 305 בקומת הכניסה");
        insertData("בניין 401 - הספריה המרכזית על שם וורצוויילר", "library", 32.067673, 34.842696, "יהדות - אולם קריאה - בקומה 1, יהדות - מדף פתוח - בקומה מינוס 2, מדעי המידע - קומת יציע, ארכיון המכון לחקר הציונות הדתית - בקומת הביניים, יהדות - יידיש - בקומת הביניים.");
        insertData("ספריית מוסיקה", "library", 32.074029, 34.84763, "ממוקמת בבניין 1005 בחדר 114");
        insertData("ספריות בבניין 502 חדר 5", "library", 32.070498, 34.843164, "קיימות בבניין ארבע ספריות: ללימודי אסיה, לערבית, לצרפתית ,לתרגום וחקר התרגום");
        insertData("ספריית פסיכולוגיה", "library", 32.072906, 34.846379, "ממוקמת בבניין 902 בקומה 1, חדר 105");
        insertData("ספריית חינוך", "library", 32.073125, 34.845797, "ממוקמת בבניין 905 בקומת הכניסה");
        insertData("מדרשה לנשים", "library", 32.068431, 34.843837, "ממוקמת בבניין 405 בחדר 105");
        insertData("ספריות כימיה ופיזיקה", "library", 32.066562, 34.840917, "ממוקמת בבניין 211 בקומת הכניסה, חדר 103");
        insertData("ספרייה למדעי החברה", "library", 32.068064, 34.841826, "ממוקמת בבניין 213 בקומת הכניסה");
        insertData("ספריית היסטוריה כללית ומזרח תיכון", "library", 32.070697, 34.843205, "ממוקמת בבניין 502 בחדר 27");
        insertData("ספריית פילוסופיה", "library", 32.073811, 34.846757, "ממוקמת בבניין 1002 בקומה 1");
        insertData("ספריית מדעי המחשב ומתמטיקה", "library", 32.068545, 34.842038, "ממוקמת בבניין 216 בקומת כניסה");
        insertData("ספריית הנדסה", "library", 32.073062, 34.849268, "ממוקמת בבניין 1104 בחדר 142");
        insertData("ספריות בבניין 404", "library", 32.068741, 34.843176, "ספריית בלשנות וספרות אנגלית - בקומה העליונה, ספריית ספרות - בקומת קרקע");
        insertData("ספריית בית המדרש", "library", 32.069134, 34.844462, "ממוקמת בבניין 411 בחדר 11");
        insertData("ספריית כלכלה ומנהל עסקים", "library", 32.069712, 34.844343, "ממוקמת בבניין 504 בקומת קרקע");
        insertData("ספריית מכון קרליבך", "library", 32.071056, 34.843306, "ממוקמת בבניין 502 בחדר 33");

//microwaves
        insertData("בניין 505 - מיקרוגל", "microwave", 32.07044, 34.844542, "עמדת חימום בבניין 505 בקומה 1");
        insertData("בניין 504 - מיקרוגל", "microwave", 32.069731,  34.844364, "עמדת חימום בבניין 504 בקומה מינוס 1");
        insertData("בניין 605 - מיקרוגל", "microwave", 32.070343, 34.843604, "עמדת חימום בבניין 605 בקומה מינוס 1");
        insertData("בניין 107 - מיקרוגל", "microwave", 32.065748, 34.842721, "עמדת חימום בבניין 107 בקומה 1");
        insertData("בניין 213 - מיקרוגל", "microwave", 32.067941, 34.841832, "עמדת חימום בבניין 213 בקומת קרקע");
        insertData("בניין 1004 - מיקרוגל", "microwave", 32.073808, 34.847427, "עמדת חימום בבניין 1004 בקומה 0");
        insertData("בניין 1102 - מיקרוגל", "microwave", 32.073402, 34.849019, "עמדת חימום בבניין 1102 בקומה 2 חדר 201");
        insertData("בניין 902 - מיקרוגל", "microwave", 32.072905, 34.846466, "עמדת חימום בבניין 902 בקומה 1");
        insertData("בניין 507 - מיקרוגלים", "microwave", 32.071195, 34.844708, "עמדות חימום בבניין 507 בקומות 0,1");
        insertData("בניין 1002 - מיקרוגל", "microwave", 32.073778, 34.846833, "עמדות חימום בבניין 1002 בקומה 3");
        insertData("בניין 305 - מיקרוגל", "microwave", 32.06699, 34.844359, "עמדת חימום בבניין 305 בקומה 0");
        insertData("בניין 410 - מיקרוגל", "microwave", 32.069037, 34.843729, "עמדת חימום בבניין 410 בקומה 2");
        insertData("בניין 905 - מיקרוגל", "microwave", 32.073119, 34.845869, "עמדת חימום בבניין 905 בקומה 4");
        insertData("בניין 604 - מיקרוגלים", "microwave", 32.070276, 34.843875, "עמדות חימום בבניין 604 בקומות 0,1");
        insertData("בניין 1105 - מיקרוגל", "microwave", 32.072933, 34.849537, "עמדת חימום בבניין 1105 בקומה 2 חדר 261");
        insertData("בניין 211 - מיקרוגל", "microwave", 32.067296, 34.841825, "עמדת חימום בבניין 211 בקומת קרקע חדר 23");
        insertData("בניין 410 - מיקרוגל", "microwave", 32.069098, 34.843713, "עמדת חימום בבניין 410 בקומה 1 חדר 129");
        insertData("בניין 101 - מיקרוגל", "microwave", 32.065825, 34.840195, "עמדת חימום בבניין 101 בקומה מינוס 1");
        insertData("בניין 404 - מיקרוגל", "microwave", 32.068739, 34.843222, "עמדת חימום בבניין 404 בקומה 0");
        insertData("בניין 105 - מיקרוגל", "microwave", 32.066, 34.841738, "עמדת חימום בבניין 105 בקומה 1");
        insertData("בניין 216 - מיקרוגל", "microwave", 32.068568, 34.842033, "עמדת חימום בבניין 216 בקומה מינוס 1 חדר 13");

//shuttles
        insertData("תחנת שאטל מספר 0", "shuttle", 32.072753, 34.849386, "תחנת שאטל");
        insertData("תחנת שאטל מספר 1", "shuttle", 32.073417, 34.848436, "תחנת שאטל");
        insertData("תחנת שאטל מספר 2", "shuttle", 32.073637, 34.846248, "תחנת שאטל");
        insertData("תחנת שאטל מספר 3", "shuttle", 32.072402, 34.844422, "תחנת שאטל");
        insertData("תחנת שאטל מספר 4", "shuttle", 32.071551, 34.843166, "תחנת שאטל");
        insertData("תחנת שאטל מספר 5", "shuttle", 32.069962, 34.842058, "תחנת שאטל");
        insertData("תחנת שאטל מספר 6", "shuttle", 32.068162, 34.840863, "תחנת שאטל");
        insertData("תחנת שאטל מספר 7", "shuttle", 32.067227, 34.840395, "תחנת שאטל");
        insertData("תחנת שאטל מספר 8", "shuttle", 32.065633, 34.842507, "תחנת שאטל");
        insertData("תחנת שאטל מספר 9", "shuttle", 32.065806, 34.84374, "תחנת שאטל");
        insertData("תחנת שאטל מספר 10", "shuttle", 32.066997, 34.844673, "תחנת שאטל");
        insertData("תחנת שאטל מספר 11", "shuttle", 32.069628, 34.843817, "תחנת שאטל");
        insertData("תחנת שאטל מספר 12", "shuttle", 32.069589, 34.842443, "תחנת שאטל");
        insertData("תחנת שאטל מספר 13", "shuttle", 32.070903, 34.842864, "תחנת שאטל");
        insertData("תחנת שאטל מספר 14", "shuttle", 32.072387, 34.844689, "תחנת שאטל");
        insertData("תחנת שאטל מספר 15", "shuttle", 32.073475, 34.845646, "תחנת שאטל");
        insertData("תחנת שאטל מספר 16", "shuttle", 32.072358, 34.847967, "תחנת שאטל");

//water
        insertData("בניין 905 - עמדת מים", "water", 32.073118, 34.84586, "עמדת מים בבניין 905 בקומה 4");
        insertData("בניין 305- עמדת מים", "water", 32.067036, 34.8444, "עמדת מים בבניין 305 בקומה 0");
        insertData("בניין 1105 - עמדת מים", "water", 32.073094, 34.849425, "עמדת מים בבניין 1105 בקומה 2 חדר 261");
        insertData("בניין 211 - עמדת מים", "water", 32.067341, 34.841861, "עמדת מים בבניין 211 בקומת קרקע חדר 23");
        insertData("בניין 604 - עמדת מים", "water", 32.070294, 34.843888, "עמדת מים בבניין 604 בקומה 2");
        insertData("בניין 1002 - עמדת מים", "water", 32.0738, 34.846818, "עמדת מים בבניין 1002 בקומה 3");
        insertData("בניין 502 - עמדת מים", "water", 32.070698, 34.843302, "עמדת מים בבניין 502 בכיתה 19");
        insertData("בניין 902 - עמדת מים", "water", 32.07289, 34.846472, "עמדת מים בבניין 902 בקומה 1");
        insertData("בניין 101 - עמדת מים", "water", 32.065850, 34.840047, "עמדת מים בבניין 101 בקומה מינוס 1");
        insertData("בניין 107 - עמדת מים", "water", 32.065748, 34.842737, "עמדת מים בבניין 107 בקומה 1");
        insertData("בניין 509 - עמדת מים", "water", 32.072002, 34.844047, "עמדת מים בבניין 509 - הקובייה המרכזית");
        insertData("בניין 216 - עמדת מים", "water", 32.068556, 34.842102, "עמדת מים בבניין 216 בקומה מינוס 1 חדר 13");

//refigerators
        insertData("בניין 101 - מקרר", "refrigerator", 32.065907, 34.840212, "מקרר בבניין 101 בקומה מינוס 1");
        insertData("בניין 107 - מקרר", "refrigerator", 32.065753, 34.842724, "מקרר בבניין 107 בקומה 1");
        insertData("בניין 905 - מקרר", "refrigerator", 32.073174, 34.845839, "מקרר בבניין 905 בקומה 4");
        insertData("בניין 211 - מקרר", "refrigerator", 32.067317, 34.841873, "מקרר בבניין 211 בקומת קרקע חדר 23");
        insertData("בניין 216 - מקרר", "refrigerator", 32.068589, 34.842065, "מקרר בבניין 216 בקומה מינוס 1 חדר 13");
        insertData("בניין 1105 - מקרר", "refrigerator", 32.072941, 34.849546, "מקרר בבניין 1105 בקומה 2 חדר 261");
        insertData("בניין 1102 - מקרר", "refrigerator", 32.073403, 34.849005, "מקרר בבניין 1102 בקומה 2 חדר 201");
        insertData("בניין 509 - מקרר", "refrigerator", 32.072012, 34.844076, "מקרר בבניין 509 - הקובייה המרכזית");
        insertData("בניין 902 - מקרר", "refrigerator", 32.072901, 34.846441, "מקרר בבניין 902 בקומה 1");
        insertData("בניין 604 - מקרר", "refrigerator", 32.070309, 34.843871, "מקרר בבניין 604 בקומה 2");
        insertData("בניין 305 - מקרר", "refrigerator", 32.067018, 34.844363, "מקרר בבניין 305 בקומה 0");
        insertData("בניין 504 - מקרר", "refrigerator", 32.069738, 34.844336, "מקרר בבניין 504 בקומה מינוס 1");

//parkings
        insertData("חניון אלקטרה", "parking", 32.073719, 34.849338, "שעות הפתיחה של חניון אלקטרה מחיר משתנות מעת לעת ומומלץ מאוד לברר עם החניון טרם ההגעה. עלות החניה של חניון אלקטרה מחיר עשויה להשתנות בהתאם לזמן השהיה ושעת ההגעה ולכן מומלץ לבדוק עם החניון.");
        insertData("חניון כלכלה", "parking", 32.069961, 34.844802, "סגור לצמיתות לרגל עבודות הרכבת הקלה.");
        insertData("חניון מוסיקה", "parking", 32.074778, 34.846915, "חניון מוסיקה ברחוב ז'בוטינסקי מאחורי בניין 1005. החנייה בחניונים אלו על בסיס יומי - מחיר כניסה חד-פעמי: 20 שקלים, לחברי אגודה המשלמים דמי רווחה: 10 שקלים. החניונים תמיד פתוחים");
        insertData("חניון יהודית", "parking", 32.071766, 34.84767, "חדש, ממוקם בסמוך לחניון וואהל.  החנייה בחניונים אלו על בסיס יומי - מחיר כניסה חד-פעמי: 20 שקלים, לחברי אגודה המשלמים דמי רווחה: 10 שקלים. החניונים תמיד פתוחים");
        insertData("חניון וואהל", "parking", 32.071505, 34.846167, "מול שער 5. החנייה בחניונים אלו על בסיס יומי - מחיר כניסה חד-פעמי: 20 שקלים, לחברי אגודה המשלמים דמי רווחה: 10 שקלים. החניונים תמיד פתוחים");
        insertData("חניון ספורט", "parking", 32.070666, 34.84921, "ברחוב מקס ואנה ווב בסמוך לשער 10. החנייה בחניונים אלו על בסיס יומי - מחיר כניסה חד-פעמי: 20 שקלים, לחברי אגודה המשלמים דמי רווחה: 10 שקלים. החניונים תמיד פתוחים");

//sports
        insertData("בניין 501 - אולם כדורסל וכדור-עף/בדמינטון", "sports", 32.069777, 34.842636, "אולם כדורסל וכדור-עף/בדמינטון. דרוש תיאום מראש לשעות הכניסה ולאחר מכן ניתן לקחת מפתח ובסוף להחזיר אותו.");
        insertData("מגרשי כדורסל וכדורגל", "sports", 32.070253, 34.842769, "מגרשים שתמיד פתוחים, אם צריך ניתן להדליק אורות.");


//restaurants
        insertData("מסעדות מחוץ לשער המעונות החדשים", "restaurant", 32.072041, 34.850302, "ביציאה משער 15(שער המעונות החדשים) יש מספר מסעדות");
        insertData("מסעדת קרנף", "restaurant", 32.066954, 34.841164, "בבניין ננו (206), קפיטריה צמחונית וחלבית. שעות פעילות: ימים א'-ה' בשעות 18:00 - 7:30.");
        insertData("מסעדת רוזמרין", "restaurant", 32.069214, 34.842853, "בבניין 409. מסעדה צמחונית(חלבי/פרווה), פתוחה בימים א'-ה', בשעות 17:00-8:00.");
        insertData("מסעדות מחוץ לשער הראשי", "restaurant", 32.067975, 34.845497, "ביציאה מהשער הראשי, קיים המרכז המסחרי של רמת אילן שבו מספר מסעדות, סופר, ועוד");
        insertData("קפה אינטרנט", "restaurant", 32.068428, 34.843856, "קפיטריה חלבית ופרווה, בגשר המדרשה (405), פתוחה בימים א'-ה', בשעות 18:00-7:00 וביום ו'-13:00-7:00. בנוסף פועל במקום פס חם לממכר אוכל חם המוגש בצלחות, מנות טבעוניות או צמחוניות.");
        insertData("פוד-טראק", "restaurant", 32.073718, 34.847114, "ברחבת בניין 1004, מסעדה בשרית/פרווה, פתוחה בימים א'-ה', בשעות 16:00-11:00.");
        insertData("מסעדת MOOD", "restaurant", 32.068122, 34.842941, "במדשאה מאחורי 402. מסעדה בשרית, פתוחה בימים א'-ה' בין השעות 10:00-15:00.");

//coffee
        insertData("קופי טיים פירמידה", "coffee", 32.068691, 34.843615, "סמוך לבניין יהדות 410");
        insertData("קופי טיים משפטים", "coffee", 32.067601, 34.844319, "בבניין 306");
        insertData("בניין 507 - עמדת קפה שטראוס עלית", "coffee", 32.071274, 34.844677, "בניין 507, קומה 0, המסדרון הצפוני");
        insertData("בניין 905 - עמדת קפה שטראוס עלית", "coffee", 32.073106, 34.845851, "בניין 905, קומת כניסה");
        insertData("בניין 1105 - עמדת קפה שטראוס עלית", "coffee", 32.072916, 34.849465, "בניין 1105, קומת כניסה");
        insertData("בניין 213 - עמדת קפה שטראוס עלית", "coffee", 32.068015, 34.841855, "בניין 213, קומה 0 מול הספרייה");
        insertData("בניין 604 - עמדת קפה שטראוס עלית", "coffee", 32.070293, 34.843819, "בניין 604, קומה מינוס 1");

//gates
        insertData("שער 20 - מוסיקה - הולגי רגל", "gate", 32.073975, 34.848392, "שעות פעילות בימי חול: 6:30-18:00, שעות פעילות בשישי וערבי חג: 6:30-14:00, שעות פעילות בשבת/חג: סגור");
        insertData("שער 25 - הרצוג", "gate", 32.072274, 34.844254, "שעות פעילות בימי חול: 6:30-18:00, שעות פעילות בשישי וערבי חג: 6:30-13:00, שעות פעילות בשבת/חג: סגור");
        insertData("שער 30 - גהה", "gate", 32.067263, 34.840335, "שעות פעילות בימי חול: 6:30 עד חצות, שעות פעילות בשישי וערבי חג: 6:30-14:00, שעות פעילות בשבת/חג: מיציאת שבת עד חצות");
        insertData("שער 2 - בנק מזרחי", "gate", 32.068179, 34.845065, "שעות פעילות בימי חול: 6:30-22:00, שעות פעילות בשישי וערבי חג: 6:30-14:00, שעות פעילות בשבת/חג: סגור");
        insertData("שער 10 - ספורט - הולגי רגל", "gate", 32.072033, 34.84826, "שעות פעילות בימי חול: 6:30-18:00, שעות פעילות בשישי וערבי חג: 6:30-14:00, שעות פעילות בשבת/חג: סגור");
        insertData("שער 15 - מעונות סטודנטים", "gate", 32.071623, 34.850185, "שעות פעילות בימי חול: 7:30 עד חצות, שעות פעילות בשישי וערבי חג: 7:30 בשישי עד חצות במוצאי שבת, שעות פעילות בשבת/חג: עד חצות במוצאי שבת");        insertData("שער 20 - מוסיקה - רכב", "gate", 32.073988, 34.848351, "ליציאה בלבד. שעות פעילות בימי חול: 6:30-18:00, שעות פעילות בשישי וערבי חג: 6:30-14:00, שעות פעילות בשבת/חג: סגור");
        insertData("שער 5 - דקלים", "gate", 32.071822, 34.845492, "שעות פעילות בימי חול: 6:30-20:00, שעות פעילות בשישי וערבי חג: 6:30-14:00, שעות פעילות בשבת/חג: סגור");
        insertData("שער 10 - ספורט - רכב", "gate", 32.071964, 34.848372, "שעות פעילות בימי חול: 6:30-18:00, שעות פעילות בשישי וערבי חג: 6:30-14:00, שעות פעילות בשבת/חג: סגור");
        insertData("שער 1 - ראשי - רכב", "gate", 32.067841, 34.844974, "שעות פעילות בימי חול: 24 שעות, שעות פעילות בשישי וערבי חג: 24 שעות, שעות פעילות בשבת/חג: 24 שעות");
        insertData("שער 40 - אונו", "gate", 32.065867, 34.843775, "שעות פעילות בימי חול: 6:30-18:00, שעות פעילות בשישי וערבי חג: 6:30-12:00, שעות פעילות בשבת/חג: סגור");

//shops
        insertData("חנות ספרים וציוד משרדי - מכלול", "shop", 32.065823, 34.842774, "בחנות מכלול תוכלו לרכוש כלי כתיבה, ציוד משרדי, מוצרי אלקטרוניקה ומחשבים, וכן צעצועים, משחקים ומתנות.  החנות נמצאת במרכז המסחרי שבבית הסטודנט (בניין 107), ופתוחה בימים א'-ה' בשעות 17:00-9:00.");
        insertData("בנק מזרחי-טפחות", "shop", 32.069091, 34.844444, "ממוקם זמנית בבניין 411 (המכון הגבוה לתורה). פועל בימים א', ג', ה' בשעות 8:30-14:00; ב', ד' בשעות 8:30-13:30 ו-15:30-17:00;  ו' וערבי חג 8:30-12:00; טלפון: 076-8040140, 8860.");
        insertData("מתוק לי", "shop", 32.070759, 34.850126, "חנות ממתקים הפועלת בימי חול מהשעה 7:00 עד 00:00, בימי שישי עד 16:00 ובשבת שעה לאחר צאת שבת.");
        insertData("מובייל פקטורי", "shop", 32.070925, 34.850355, "חנות למוצרי סלולר");
        insertData("המפעיל - צילום מסמכים ושירותי דפוס", "shop", 32.068381, 34.843568, " מרכז שירות מתקדם לצילום, הדפסה, כריכה, עיצוב וגרפיקה. הסניף המרכזי נמצא בבניין 405 בית הדפוס פועל בין הימים א'-ה בשעות 8:00-17:00, טלפון: 03-5317487.");
        insertData("סנדלרייה ושכפול מפתחות", "shop", 32.065757, 34.842534, "תיקוני נעליים, בגדים, רוכסנים ותיקים. מכירת סוללות, רצועות לשעונים, משחות, רפידות, שרוכים ושכפול מפתחות. מיקום ושעות פתיחה: בניין אגודת הסטודנטים (בניין 107), ימים א'-ה' 16:00-09:00.");
        insertData("מספרה - אפי בן מרדכי", "shop", 32.071118, 34.850315, "מספרה בסמוך לקמפוס");
        insertData("מינימרקט אומש", "shop", 32.06582, 34.842838, "בבית הסטודנט (בניין 107), פתוח בימים א'-ד' בשעות 7:30-18:30. במקום נמכרים לצד מוצרי מזון וחומרי ניקוי, גם כריכים, סלטים, ארוחות מוכנות לחימום, טוסטים, פיצה, מאפים ומשקאות חמים.");
        insertData("מרקט-אקספרס", "shop", 32.070803, 34.850224, "בקמפוס המעונות הצפוני, מצדו החיצוני של המבנה שפתוח בשעות 7:30-23:00");

    }




    public boolean insertData(String name, String type, double location_lat, double location_lon, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, name);
        values.put(KEY_TYPE, type);
        values.put(KEY_LOCATION_LAT, location_lat);
        values.put(KEY_LOCATION_LON, location_lon);
        values.put(KEY_DESCRIPTION, description);
        /*long result = */
        long result = db.insert(MARKERS_TABLE_NAME, null, values);
        db.close();

        if(result == -1)
        {
            return false;
        }
        return true;
    }

    public boolean editData(String name, String field, String newValue) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if(field.equals(this.getMarkersFields()[2]) || field.equals(this.getMarkersFields()[3])) {
            contentValues.put(field, Double.parseDouble(newValue));
        }

        else {
            contentValues.put(field, newValue);
        }

        int result = db.update(MARKERS_TABLE_NAME, contentValues, KEY_NAME + "=?", new String[]{name});

        if(result == 0)
        {
            return false;
        }
        return true;
        //SQLiteDatabase db = this.getWritableDatabase();
//
        //ContentValues values = new ContentValues();
//
        //values.put(KEY_NAME, name);
        //values.put(KEY_TYPE, type);
        //values.put(KEY_LOCATION_LAT, location_lat);
        //values.put(KEY_LOCATION_LON, location_lon);
        //values.put(KEY_DESCRIPTION, description);
        ///*long result = */
        //db.insert(MARKERS_TABLE_NAME, null, values);
        //db.close();

        /*if(result == -1)
        {
            return false;
        }
        return true;*/
    }

    public boolean removeData(String name) {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(MARKERS_TABLE_NAME, KEY_NAME + "=?", new String[]{name});

        if(result == 0)
        {
            return false;
        }
        return true;

        //SQLiteDatabase db = this.getWritableDatabase();
//
        //ContentValues values = new ContentValues();
//
        //values.put(KEY_NAME, name);
        //values.put(KEY_TYPE, type);
        //values.put(KEY_LOCATION_LAT, location_lat);
        //values.put(KEY_LOCATION_LON, location_lon);
        //values.put(KEY_DESCRIPTION, description);
        ///*long result = */
        //db.insert(MARKERS_TABLE_NAME, null, values);
        //db.close();

        /*if(result == -1)
        {
            return false;
        }
        return true;*/
    }


    public Cursor getAllMarkers() {
        String selectQuery = "SELECT  * FROM " + MARKERS_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM markers", null);

        //if the markers added to the DB
        if (cursor.moveToNext()) {
            return cursor;
        }
        //if the DB is empty - adding the markers
        else {
            addAllMarkers();
            return null;
        }
    }

    public String[] getMarkersFields() {
        return new String[] {KEY_NAME, KEY_TYPE, KEY_LOCATION_LAT, KEY_LOCATION_LON, KEY_DESCRIPTION};
    }

    public void insertUser(String username, String password, String name, String user_type, String connection_type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_NAME, name);
        values.put(KEY_USER_TYPE, user_type);
        values.put(KEY_CONNECTION_TYPE, connection_type);

        db.insert(USERS_TABLE_NAME, null, values);
        db.close();
    }

    public void addAdminUsers() {
        insertUser("admin", "admin123", "admin1", "admin", "regular");
    }

    public boolean doesUserExists(String username, String connection_type) {
        String selectQuery = "SELECT  * FROM " + USERS_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM markers", null);

        //if the markers added to the DB
        if (cursor.moveToNext()) {
            boolean usernExists = false;
            //check if username doest exists
            if (cursor != null) {
                do {
                    if(cursor.getString(0).equals(username) && cursor.getString(4).equals(connection_type)) {
                        usernExists = true;
                    }

                } while (cursor.moveToNext());

            }
            return usernExists;
        }
        //if the DB is empty - adding admin users
        else {
            addAdminUsers();
            return doesUserExists(username, connection_type);
        }
    }

    public Pair<Boolean, Pair<String,String>> doesAccountExists(String username, String password) {
        String selectQuery = "SELECT  * FROM " + USERS_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM markers", null);

        //if the markers added to the DB
        if (cursor.moveToNext()) {
            String name = "";
            String userType = "";
            boolean accountExists = false;
            //check if username doest exists
            if (cursor != null) {
                do {
                    if(cursor.getString(0).equals(username) && cursor.getString(1).equals(password)) {
                        accountExists = true;
                        name = cursor.getString(2);
                        userType = cursor.getString(3);
                    }

                } while (cursor.moveToNext());

            }
            return new Pair<Boolean, Pair<String,String>>(accountExists, new Pair<String, String>(name, userType));
        }
        //if the DB is empty - adding admin users
        else {
            addAdminUsers();
            return doesAccountExists(username, password);
        }
    }

    public boolean createAccount(String username, String password, String name, String user_type, String connection_type) {
        if(!doesUserExists(username, connection_type)) {
            insertUser(username, password, name, user_type, connection_type);
            return true;
        }
        return false;

        //String selectQuery = "SELECT  * FROM " + USERS_TABLE_NAME;
//
        //SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);
//
        ////SQLiteDatabase db = this.getReadableDatabase();
        ////Cursor cursor = db.rawQuery("SELECT * FROM markers", null);
//
        ////if the markers added to the DB
        //if (cursor.moveToNext()) {
        //    boolean usernameExists = false;
        //    //check if username doest exists
        //    if (cursor != null) {
        //        do {
        //            if(cursor.getString(0).equals(username)) {
        //                usernameExists = true;
        //            }
//
        //        } while (cursor.moveToNext());
//
        //    }
        //    if(usernameExists) {
        //        return false;
        //    }
//
        //    else {
        //        insertUser(username, password, name, user_type, connection_type);
        //        return true;
        //    }
        //}
        ////if the DB is empty - adding admin users
        //else {
        //    addAdminUsers();
        //    addNewUser(username, password, name, user_type, connection_type);
        //}
        //return false;
    }

    public void addFavorite(String username, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_USERNAME, username);
        values.put(KEY_NAME, name);

        db.insert(FAVORITES_TABLE_NAME, null, values);
        db.close();
    }

    public boolean doesFavoriteExists(String username, String name) {
        String selectQuery = "SELECT * FROM " + FAVORITES_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM markers", null);

        //if the markers added to the DB
        if (cursor.moveToNext()) {
            boolean faveExists = false;
            //check if username doest exists
            if (cursor != null) {
                do {
                    if(cursor.getString(0).equals(username) && cursor.getString(1).equals(name)) {
                        faveExists = true;
                    }

                } while (cursor.moveToNext());

            }
            return faveExists;
        }
        //if the DB is empty - adding admin users
        else {
            return false;
        }
    }
}
