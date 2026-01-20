package customer.cnma_s4_integration_gateway.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
public class DataUtil {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    
    public static Boolean isNumeric(String numeric) {
        return numeric.matches("-?\\d+(\\.\\d+)?"); // Matches integers and decimal numbers
    }

    public static Boolean isIntegerNumeric(String numeric){  
        try {
            Integer.parseInt(numeric);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
        }
        return false;
    }

    public static Boolean isDoubleNumeric(String numeric){  
        try {
            Double.parseDouble(numeric);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Double.");
        }
        return false;
    }

    public static Boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static Boolean isNull(Object obj) {
        return obj == null;
    }

    public static Boolean isAnyNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }


    public static Boolean isNullOrBlank(String value) {
        return (value == null || value == "") ;
    }

    public static Boolean isAnyNullOrEmpty(String... strings) {
        for (String so: strings) {
            if (so == null || so == "") {
                return true;
            }
        }
        return false;
    }

    public static boolean isInRange(int number, int lowerBound, int upperBound) {
        return number >= lowerBound && number <= upperBound;
    }

    public static Boolean isTimeStampFormatData(String data){
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            // Try to parse the input string as a Timestamp
            new Timestamp(dateFormat.parse(data).getTime());
            // If parsing is successful, it's a valid Timestamp
            return true;
        } catch (ParseException e) {
            // Parsing failed, it's not a valid Timestamp
            return false;
        }
    }

    public static String convertStringToTimestamp(String dateString) {

        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = inputDateFormat.parse(dateString);

            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String formattedDate = outputDateFormat.format(date);

            return formattedDate;
        } catch (Exception e) {
            // Handle parsing errors
            e.printStackTrace();
            return null; // Return null to indicate failure
        }
    }

    public static String convertStringDateLength8ToValidFormat(String dateString) {

        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd");
            java.util.Date date = inputDateFormat.parse(dateString);

            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = outputDateFormat.format(date);

            return formattedDate;
        } catch (Exception e) {
            // Handle parsing errors
            e.printStackTrace();
            return null; // Return null to indicate failure
        }
    }

    public static void removeProperty(Object object, String propertyName) throws Exception {
        Class<?> clazz = object.getClass();
        Field field = clazz.getDeclaredField(propertyName);
        field.setAccessible(true);
        field.set(object, null); // Set the value to null to effectively "remove" the property
    }

    public static byte[] serializeObject(Serializable obj) throws IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(obj);
            return byteStream.toByteArray();
        }
    }
    
    public static boolean isEmailValidFormat(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }


    public static String generateRandomUUID(){
        return UUID.randomUUID().toString();
    }

    public static Integer safeParseInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal toBigDecimal(Object value) {
        if (value instanceof java.math.BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return BigDecimal.valueOf(((Number) value).doubleValue());
        return null;
    }

    public static BigDecimal safeBigDecimal(String value) {
        return (value != null && value != "") ? new BigDecimal(value) : null;
    }

    
    public static String formatLocalDateToSAPDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        Instant instant = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
        long epochMillis = instant.toEpochMilli();
        return "/Date(" + epochMillis + ")/";
    }

    public static String extractODataMessageFromXmlString(String xmlBody) {

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(xmlBody.getBytes(StandardCharsets.UTF_8)));

            NodeList messages = doc.getElementsByTagName("message");
            for (int i = 0; i < messages.getLength(); ) {
                String msg = messages.item(i).getTextContent();
                    return msg;
                // }
            }
            if (messages.getLength() > 0) {
                return messages.item(0).getTextContent();
            }
        } catch (Exception e) {
            return "Failed to parse SAP error message";
        }
        return "Unknown SAP error";
    }
    
}
