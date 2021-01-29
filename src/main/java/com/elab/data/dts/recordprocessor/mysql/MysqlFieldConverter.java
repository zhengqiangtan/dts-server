package com.elab.data.dts.recordprocessor.mysql;

import com.elab.data.dts.formats.avro.*;
import com.elab.data.dts.formats.avro.Character;
import com.elab.data.dts.formats.avro.Float;
import com.elab.data.dts.formats.avro.Integer;
import com.elab.data.dts.recordprocessor.FieldConverter;
import com.elab.data.dts.recordprocessor.FieldValue;

import java.nio.ByteBuffer;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

public class MysqlFieldConverter implements FieldConverter {
    @Override
    public FieldValue convert(Field field, Object o) {
        return DATA_ADAPTER[field.getDataTypeNumber()].getFieldValue(o);
    }


    static DataAdapter[] DATA_ADAPTER = new DataAdapter[256];

    static {
        DATA_ADAPTER[0] = new DecimalStringAdapter(); //Type.DECIMAL
        DATA_ADAPTER[1] = new NumberStringAdapter(); //Type.INT8;
        DATA_ADAPTER[2] = new NumberStringAdapter(); //Type.INT16;
        DATA_ADAPTER[3] = new NumberStringAdapter(); //Type.INT32;

        DATA_ADAPTER[4] = new DoubleStringAdapter(); //Type.FLOAT
        DATA_ADAPTER[5] = new DoubleStringAdapter(); //Type.DOUBLE

        DATA_ADAPTER[6] = new UTF8StringEncodeAdapter(); //Type.NULL

        DATA_ADAPTER[7] = new TimestampStringAdapter(); //Type.TIMESTAMP
        DATA_ADAPTER[8] = new NumberStringAdapter(); //Type.INT64
        DATA_ADAPTER[9] = new NumberStringAdapter(); //Type.INT24

        DATA_ADAPTER[10] = new DateAdapter(); //Type.DATE
        DATA_ADAPTER[11] = new TimeAdapter(); //Type.TIME
        DATA_ADAPTER[12] = new DateTimeAdapter(); //Type.DATETIME
        DATA_ADAPTER[13] = new YearAdapter(); //Type.YEAR
        DATA_ADAPTER[14] = new DateTimeAdapter(); //Type.DATETIME
        DATA_ADAPTER[15] = new CharacterAdapter(); 	//Type.STRING
        DATA_ADAPTER[16] = new NumberStringAdapter(); //Type.BIT

        DATA_ADAPTER[255] = new GeometryAdapter(); 	//Type.GEOMETRY;
        DATA_ADAPTER[254] = new CharacterAdapter(); //Type.STRING;
        DATA_ADAPTER[253] = new CharacterAdapter(); //Type.STRING;

        DATA_ADAPTER[252] = new BinaryAdapter(); //Type.BLOB;
        DATA_ADAPTER[251] = new BinaryAdapter(); //Type.BLOB;
        DATA_ADAPTER[250] = new BinaryAdapter(); //Type.BLOB;
        DATA_ADAPTER[249] = new BinaryAdapter(); //Type.BLOB;

        DATA_ADAPTER[246] = new DecimalStringAdapter(); //Type.DECIMAL;

        DATA_ADAPTER[248] = new TextObjectAdapter(); //Type.SET;
        DATA_ADAPTER[247] = new TextObjectAdapter(); //Type.ENUM;
        DATA_ADAPTER[245] = new TextObjectAdapter();  //Type.JSON;
    }


    static interface DataAdapter {

        FieldValue getFieldValue(Object data);
    }


    static class UTF8StringEncodeAdapter implements DataAdapter {
        @Override
        public FieldValue getFieldValue(Object data) {

            FieldValue fieldValue = new FieldValue();
            if (null != data) {
                byte[] bytes = ((String) data).getBytes(UTF_8);
                fieldValue.setValue(bytes);
            }
            fieldValue.setEncoding("UTF8");
            return fieldValue;
        }
    }

    static class NumberStringAdapter implements DataAdapter {
        @Override
        public FieldValue getFieldValue(Object data) {
            FieldValue fieldValue = new FieldValue();
            if (null != data) {
                Integer integer = (Integer) data;
                fieldValue.setValue(integer.getValue().getBytes(US_ASCII));
            }
            fieldValue.setEncoding("ASCII");
            return fieldValue;
        }
    }

    static class DecimalStringAdapter implements DataAdapter {
        @Override
        public FieldValue getFieldValue(Object data) {
            FieldValue fieldValue = new FieldValue();
            if (null != data) {
                Decimal decimal = (Decimal) data;
                fieldValue.setValue(decimal.getValue().getBytes(US_ASCII));
            }
            fieldValue.setEncoding("ASCII");
            return fieldValue;
        }
    }

    static class DoubleStringAdapter implements DataAdapter {
        @Override
        public FieldValue getFieldValue(Object data) {
            FieldValue fieldValue = new FieldValue();
            if (null != data) {
                Float aFloat = (Float) data;
                fieldValue.setValue(Double.toString(aFloat.getValue()).getBytes(US_ASCII));
            }
            fieldValue.setEncoding("ASCII");
            return fieldValue;
        }
    }

    static class TimestampStringAdapter implements DataAdapter {
//        static String[] MILLIS_PREFIX = new String[]{"", "0", "00", "000", "0000", "00000", "000000"};
        @Override
        public FieldValue getFieldValue(Object data) {
            FieldValue fieldValue = new FieldValue();
            if (null != data) {
//                StringBuilder timestampBuilder = new StringBuilder();
                Timestamp timestamp = (Timestamp) data;
                fieldValue.setValue(timestamp.getTimestamp().toString().getBytes(US_ASCII));

//                timestampBuilder.append(timestamp.getTimestamp());
//                if (null != timestamp.getMillis() && timestamp.getMillis() > 0) {
//                    timestampBuilder.append('.');
//                    String millis = java.lang.Integer.toString(timestamp.getMillis());
//                    timestampBuilder.append(MILLIS_PREFIX[6 - millis.length()]).append(millis);
//                }

//                fieldValue.setValue(timestampBuilder.toString().getBytes(US_ASCII));
            }
            fieldValue.setEncoding("ASCII");
            return fieldValue;
        }
    }

    static abstract class AbstractDateTimeAdapter implements DataAdapter {

        void encodeDate(DateTime dateTime, byte[] out, int position) {
            if (null != dateTime && null != out) {
                out[position] = (byte) ('0' + (dateTime.getYear() / 1000));
                out[position + 1] = (byte) ('0' + (dateTime.getYear() % 1000 / 100));
                out[position + 2] = (byte) ('0' + (dateTime.getYear() % 100 / 10));
                out[position + 3] = (byte) ('0' + (dateTime.getYear() % 10));
                out[position + 4] = '-';
                out[position + 5] = (byte) ('0' + (dateTime.getMonth() / 10));
                out[position + 6] = (byte) ('0' + (dateTime.getMonth() % 10));
                out[position + 7] = '-';
                out[position + 8] = (byte) ('0' + (dateTime.getDay() / 10));
                out[position + 9] = (byte) ('0' + (dateTime.getDay() % 10));
            }
        }

        void encodeTime(DateTime dateTime, byte[] out, int position) {
            if (null != dateTime && null != out) {
                out[position + 0] = (byte) ('0' + (dateTime.getHour() / 10));
                out[position + 1] = (byte) ('0' + (dateTime.getHour() % 10));
                out[position + 2] = ':';
                out[position + 3] = (byte) ('0' + (dateTime.getMinute() / 10));
                out[position + 4] = (byte) ('0' + (dateTime.getMinute() % 10));
                out[position + 5] = ':';
                out[position + 6] = (byte) ('0' + (dateTime.getSecond() / 10));
                out[position + 7] = (byte) ('0' + (dateTime.getSecond() % 10));
            }
        }

        void encodeTimeMillis(DateTime dateTime, byte[] out, int position) {
            if (null != dateTime.getMillis() && 0 != dateTime.getMillis()) {
                int mills = dateTime.getMillis();
                out[position] = '.';
                out[position + 1] = (byte) ('0' + (mills / 100000));
                mills %= 100000;
                out[position + 2] = (byte) ('0' + (mills / 10000));
                mills %= 10000;
                out[position + 3] = (byte) ('0' + (mills / 1000));
                mills %= 1000;
                out[position + 4] = (byte) ('0' + (mills / 100));
                mills %= 100;
                out[position + 5] = (byte) ('0' + (mills / 10));
                out[position + 6] = (byte) ('0' + (mills % 10));
            }
        }
    }

    static class DateAdapter extends AbstractDateTimeAdapter {

        @Override
        public FieldValue getFieldValue(Object data) {

            FieldValue fieldValue = new FieldValue();
            if (null != data) {
                DateTime dateTime = (DateTime) data;

                byte[] date = new byte[10];
                encodeDate(dateTime, date, 0);

                fieldValue.setValue(date);
            }

            fieldValue.setEncoding("ASCII");
            return fieldValue;
        }
    }


    static class TimeAdapter extends AbstractDateTimeAdapter {

        @Override
        public FieldValue getFieldValue(Object data) {

            FieldValue fieldValue = new FieldValue();
            if (null != data) {
                DateTime dateTime = (DateTime)data;

                // 判断是否是负数
                int head = 0;
                if (dateTime.getHour() <= -100) {
                    head = 2;
                } else if ((dateTime.getHour() >= 100)
                        || (dateTime.getHour() < 0)
                        || (dateTime.getMinute() < 0)
                        || (dateTime.getSecond() < 0)
                        || ((null != dateTime.getMillis()) && (dateTime.getMillis() < 0))) {
                    head = 1;
                }
                byte[] time;
                // 毫秒位0忽略
                if (null == dateTime.getMillis() || 0 == dateTime.getMillis()) {
                    time = new byte[8 + head];
                } else {
                    time = new byte[15 + head];
                }

                int index = 0;
                if (head > 0 && dateTime.getHour() <= 0) {
                    dateTime.setHour(-dateTime.getHour());
                    dateTime.setMinute(-dateTime.getMinute());
                    dateTime.setSecond(-dateTime.getSecond());
                    if (null != dateTime.getMillis()) {
                        dateTime.setMillis(-dateTime.getMillis());
                    }

                    time[index++] = '-';
                }

                if (dateTime.getHour() >= 100) {
                    time[index++] = (byte) ('0' + (dateTime.getHour() / 100));
                    dateTime.setHour(dateTime.getHour() % 100);
                }

                encodeTime(dateTime, time, index);
                encodeTimeMillis(dateTime, time, index + 8);

                fieldValue.setValue(time);
            }

            fieldValue.setEncoding("ASCII");
            return fieldValue;
        }
    }

    static class DateTimeAdapter extends AbstractDateTimeAdapter {

        @Override
        public FieldValue getFieldValue(Object data) {

            FieldValue fieldValue = new FieldValue();
            if (null != data) {
                DateTime dateTime = (DateTime) data;

                byte[] time = null;
                //忽略毫秒位值是0
                if (null == dateTime.getMillis() || 0 == dateTime.getMillis()) {
                    time = new byte[19];
                } else {
                    time = new byte[26];
                }
                encodeDate(dateTime, time, 0);
                time[10] = ' ';
                encodeTime(dateTime, time, 11);
                encodeTimeMillis(dateTime, time, 19);

                fieldValue.setValue(time);
            }

            fieldValue.setEncoding("ASCII");
            return fieldValue;
        }

    }


    static class YearAdapter implements DataAdapter {

        @Override
        public FieldValue getFieldValue(Object data) {

            FieldValue fieldValue = new FieldValue();
            if (null != data) {
                DateTime dateTime = (DateTime) data;
                fieldValue.setValue(java.lang.Integer.toString(dateTime.getYear()).getBytes(US_ASCII));
            }

            fieldValue.setEncoding("ASCII");
            return fieldValue;
        }

    }

    static class CharacterAdapter implements DataAdapter {

        @Override
        public FieldValue getFieldValue(Object data) {

            FieldValue fieldValue = new FieldValue();
            if (null != data) {
                Character character = (Character) data;
                fieldValue.setValue(getBytes(character.getValue()));
                fieldValue.setEncoding(character.getCharset());
            } else {
                fieldValue.setEncoding("ASCII");
            }
            return fieldValue;
        }

    }

    static class GeometryAdapter implements DataAdapter {

        @Override
        public FieldValue getFieldValue(Object data) {

            FieldValue fieldValue = new FieldValue();
            if (null != data) {
                BinaryGeometry geometry = (BinaryGeometry) data;
                fieldValue.setValue(getBytes(geometry.getValue()));
            }
            return fieldValue;
        }
    }


    static class BinaryAdapter implements DataAdapter {

        @Override
        public FieldValue getFieldValue(Object data) {

            FieldValue fieldValue = new FieldValue();
            if (null != data) {
                BinaryObject binaryObject = (BinaryObject) data;
                fieldValue.setValue(getBytes(binaryObject.getValue()));
            }
            return fieldValue;
        }
    }


    static class TextObjectAdapter implements DataAdapter {

        @Override
        public FieldValue getFieldValue(Object data) {

            FieldValue fieldValue = new FieldValue();
            if (null != data) {
                TextObject textObject = (TextObject) data;
                byte[] bytes = textObject.getValue().getBytes(UTF_8);
                fieldValue.setValue(bytes);
            }
            fieldValue.setEncoding("UTF8");
            return fieldValue;
        }
    }

    static byte[] getBytes(ByteBuffer origin) {
        byte[] ret = new byte[origin.remaining()];
        origin.get(ret);
        return ret;
    }
}
