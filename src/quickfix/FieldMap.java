/****************************************************************************
** Copyright (c) 2001-2005 quickfixengine.org  All rights reserved.
**
** This file is part of the QuickFIX FIX Engine
**
** This file may be distributed under the terms of the quickfixengine.org
** license as defined by quickfixengine.org and appearing in the file
** LICENSE included in the packaging of this file.
**
** This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
** WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
**
** See http://www.quickfixengine.org/LICENSE for licensing information.
**
** Contact ask@quickfixengine.org if any conditions of this licensing are
** not clear to you.
**
****************************************************************************/

package quickfix;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import quickfix.field.BeginString;
import quickfix.field.BodyLength;
import quickfix.field.CheckSum;
import quickfix.field.SessionRejectReason;

public abstract class FieldMap {
    private final int[] fieldOrder;
    private final TreeMap fields;
    private final TreeMap groups = new TreeMap();

    protected FieldMap(int[] fieldOrder) {
        this.fieldOrder = fieldOrder;
        //fields = new TreeMap(new FieldOrderComparator());
        fields = new TreeMap(/*new FieldOrderComparator()*/);
    }

    protected FieldMap() {
        fieldOrder = null;
        fields = new TreeMap();
    }

    protected int[] getFieldOrder() {
        return fieldOrder;
    }

    private class FieldOrderComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            Integer tag1 = (Integer) o1;
            Integer tag2 = (Integer) o2;
            int index1 = indexOf(tag1.intValue(), fieldOrder);
            int index2 = indexOf(tag2.intValue(), fieldOrder);
            return index1 != index2 ? (index1 < index2 ? -1 : 1) : 0;
        }

        private int indexOf(int tag, int[] order) {
            if (order != null) {
	            for (int i = 0; i < order.length; i++) {
	                if (tag == order[i]) {
	                    return i;
	                }
	            }
            }
            return Integer.MAX_VALUE;
        }
    }

    public void setFields(FieldMap fieldMap) {
        fields.clear();
        fields.putAll(fieldMap.fields);
    }

    public void setString(int field, String value) {
        setField(new StringField(field, value));
    }

    public void setBoolean(int field, boolean value) {
        String s = FieldValueConverter.BooleanConverter.convert(value);
        setField(new StringField(field, s));
    }

    public void setChar(int field, char value) {
        String s = FieldValueConverter.CharConverter.convert(value);
        setField(new StringField(field, s));
    }

    public void setInt(int field, int value) {
        String s = FieldValueConverter.IntConverter.convert(value);
        setField(new StringField(field, s));
    }

    public void setDouble(int field, double value) {
        String s = FieldValueConverter.DoubleConverter.convert(value);
        setField(new StringField(field, s));
    }

    public void setUtcTimeStamp(int field, Date value) {
        setUtcTimeStamp(field, value, false);
    }

    public void setUtcTimeStamp(int field, Date value, boolean includeMilliseconds) {
        String s = FieldValueConverter.UtcTimestampConverter.convert(value, includeMilliseconds);
        setField(new StringField(field, s));
    }

    public void setUtcTimeOnly(int field, Date value) {
        setUtcTimeOnly(field, value, false);
    }
    public void setUtcTimeOnly(int field, Date value, boolean includeMillseconds) {
        String s = FieldValueConverter.UtcTimeOnlyConverter.convert(value, includeMillseconds);
        setField(new StringField(field, s));
    }

    public void setUtcDateOnly(int field, Date value) {
        String s = FieldValueConverter.UtcDateOnlyConverter.convert(value, true);
        setField(new StringField(field, s));
    }

    public String getString(int field) throws FieldNotFound {
        return getField(field).getObject().toString();
    }

    StringField getField(int field) throws FieldNotFound {
        StringField f = (StringField) fields.get(new Integer(field));
        if (f == null) {
            throw new FieldNotFound(field);
        }
        return f;
    }

    Field getField(int field, Field defaultValue) {
        Field f = (Field) fields.get(new Integer(field));
        if (f == null) {
            return defaultValue;
        }
        return f;
    }

    public boolean getBoolean(int field) throws FieldNotFound {
        String value = getField(field).getValue();
        try {
            return FieldValueConverter.BooleanConverter.convert(value);
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field);
        }
    }

    public char getChar(int field) throws FieldNotFound {
        String value = getField(field).getValue();
        try {
            return FieldValueConverter.CharConverter.convert(value);
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field);
        }
    }

    public int getInt(int field) throws FieldNotFound {
        String value = getField(field).getValue();
        try {
            return FieldValueConverter.IntConverter.convert(value);
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field);
        }
    }

    public double getDouble(int field) throws FieldNotFound {
        String value = getField(field).getValue();
        try {
            return FieldValueConverter.DoubleConverter.convert(value);
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field);
        }
    }

    public Date getUtcTimeStamp(int field) throws FieldNotFound {
        String value = getField(field).getValue();
        try {
            return FieldValueConverter.UtcTimestampConverter.convert(value, false);
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field);
        }
    }

    public Date getUtcTimeOnly(int field) throws FieldNotFound {
        String value = getField(field).getValue();
        try {
            return FieldValueConverter.UtcTimeOnlyConverter.convert(value);
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field);
        }
    }

    public Date getUtcDateOnly(int field) throws FieldNotFound {
        String value = getField(field).getValue();
        try {
            return FieldValueConverter.UtcDateOnlyConverter.convert(value);
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field);
        }
    }

    public void setField(StringField field) {
        if (field.getValue() == null) {
            throw new NullPointerException("Null field values are not allowed.");
        }
        fields.put(new Integer(field.getField()), field);
    }

    public void setField(BooleanField field) {
        setBoolean(field.getField(), field.getValue());
    }

    public void setField(CharField field) {
        setChar(field.getField(), field.getValue());
    }

    public void setField(IntField field) {
        setInt(field.getField(), field.getValue());
    }

    public void setField(DoubleField field) {
        setDouble(field.getField(), field.getValue());
    }

    public void setField(UtcTimeStampField field) {
        setUtcTimeStamp(field.getField(), field.getValue());
    }

    public void setField(UtcTimeOnlyField field) {
        setUtcTimeOnly(field.getField(), field.getValue());
    }

    public void setField(UtcDateOnlyField field) {
        setUtcDateOnly(field.getField(), field.getValue());
    }

    public StringField getField(StringField field) throws FieldNotFound {
        return (StringField) getFieldInternal(field);
    }

    private Field getFieldInternal(Field field) throws FieldNotFound {
        field.setObject(getField(field.getField()).getObject());
        return field;
    }

    public BooleanField getField(BooleanField field) throws FieldNotFound {
        try {
            String value = getField(field.getField()).getValue();
            field.setObject(new Boolean(FieldValueConverter.BooleanConverter.convert(value)));
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field.getField());
        } catch (FieldNotFound e) {
            throw e;
        }
        return field;
    }

    public CharField getField(CharField field) throws FieldNotFound {
        try {
            String value = getField(field.getField()).getValue();
            field.setObject(new Character(FieldValueConverter.CharConverter.convert(value)));
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field.getField());
        } catch (FieldNotFound e) {
            throw e;
        }
        return field;
    }

    public IntField getField(IntField field) throws FieldNotFound {
        try {
            String value = getField(field.getField()).getValue();
            field.setObject(new Integer(FieldValueConverter.IntConverter.convert(value)));
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field.getField());
        } catch (FieldNotFound e) {
            throw e;
        }
        return field;
    }

    public DoubleField getField(DoubleField field) throws FieldNotFound {
        try {
            String value = getField(field.getField()).getValue();
            field.setObject(new Double(FieldValueConverter.DoubleConverter.convert(value)));
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field.getField());
        } catch (FieldNotFound e) {
            throw e;
        }
        return field;
    }

    public UtcTimeStampField getField(UtcTimeStampField field) throws FieldNotFound {
        try {
            String value = getField(field.getField()).getValue();
            // TODO - Determine how to use calculate days...
            field.setObject(FieldValueConverter.UtcTimestampConverter.convert(value, false));
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field.getField());
        } catch (FieldNotFound e) {
            throw e;
        }
        return field;
    }

    public UtcTimeOnlyField getField(UtcTimeOnlyField field) throws FieldNotFound {
        try {
            String value = getField(field.getField()).getValue();
            field.setObject(FieldValueConverter.UtcTimeOnlyConverter.convert(value));
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field.getField());
        } catch (FieldNotFound e) {
            throw e;
        }
        return field;
    }

    public UtcDateOnlyField getField(UtcDateOnlyField field) throws FieldNotFound {
        try {
            String value = getField(field.getField()).getValue();
            field.setObject(FieldValueConverter.UtcDateOnlyConverter.convert(value));
        } catch (FieldConvertError e) {
            throw newIncorrectDataException(e, field.getField());
        } catch (FieldNotFound e) {
            throw e;
        }
        return field;
    }

    private FieldException newIncorrectDataException(FieldConvertError e, int tag) {
        return new FieldException(SessionRejectReason.INCORRECT_DATA_FORMAT_FOR_VALUE, e
                .getMessage(), tag);
    }

    public boolean isSetField(int field) {
        return fields.containsKey(new Integer(field));
    }

    public boolean isSetField(Field field) {
        return isSetField(field.getField());
    }

    public void removeField(int field) {
        fields.remove(new Integer(field));
    }

    public Iterator iterator() {
        return fields.values().iterator();
    }

    protected void initializeFrom(FieldMap target) {
        target.fields.clear();
        target.fields.putAll(fields);
    }

    void calculateString(StringBuffer buffer, int[] preFields, int[] postFields) {
        if (preFields != null) {
            for (int i = 0; i < preFields.length; i++) {
                Field field = getField(preFields[i], null);
                field.toString(buffer);
                buffer.append('\001');
            }
        }
        for (Iterator iter = fields.values().iterator(); iter.hasNext();) {
            Field field = (Field) iter.next();
            if (!isOrderedField(field.getField(), preFields)
                    && !isOrderedField(field.getField(), postFields)) {
                field.toString(buffer);
                buffer.append('\001');
            }
        }
        if (postFields != null) {
            for (int i = 0; i < postFields.length; i++) {
                Field field = getField(postFields[i], null);
                field.toString(buffer);
                buffer.append('\001');
            }
        }
    }

    private boolean isOrderedField(int field, int[] fieldOrder) {
        if (fieldOrder != null) {
            for (int i = 0; i < fieldOrder.length; i++) {
                if (field == fieldOrder[i]) {
                    return true;
                }
            }
        }
        return false;
    }

    int calculateLength() {
        int result = 0;
        for (Iterator iter = fields.values().iterator(); iter.hasNext();) {
            Field field = (Field) iter.next();
            if (field.getField() == BeginString.FIELD || field.getField() == BodyLength.FIELD
                    || field.getField() == CheckSum.FIELD) {
                continue;
            }
            result += field.getLength();
        }

        Iterator iterator = groups.values().iterator();
        while (iterator.hasNext()) {
            List groupList = (List)iterator.next();
            for (int i = 0; i < groupList.size(); i++) {
                Group group = (Group)groupList.get(i);
                result += group.calculateLength();
            }
        }
        
        //      Groups::const_iterator j;
        //      for ( j = m_groups.begin(); j != m_groups.end(); ++j )
        //      {
        //        std::vector < FieldMap* > ::const_iterator k;
        //        for ( k = j->second.begin(); k != j->second.end(); ++k )
        //          result += ( *k ) ->calculateLength();
        //      }
        return result;

    }

    int calculateTotal() {

        int result = 0;
        for (Iterator iter = fields.values().iterator(); iter.hasNext();) {
            Field field = (Field) iter.next();
            if (field.getField() == CheckSum.FIELD) {
                continue;
            }
            result += field.getTotal();
        }

        //      Groups::const_iterator j;
        //      for ( j = m_groups.begin(); j != m_groups.end(); ++j )
        //      {
        //        std::vector < FieldMap* > ::const_iterator k;
        //        for ( k = j->second.begin(); k != j->second.end(); ++k )
        //          result += ( *k ) ->calculateTotal();
        //      }
        return result;
    }

    int getGroupCount(int tag) {
        return getGroups(tag).size();
    }

    public Iterator groupKeyIterator() {
        return groups.keySet().iterator();
    }
    
    public void addGroup(Group group) {
        getGroups(group.getField()).add(new Group(group));
    }

    /*package*/ List getGroups(int field) {
        List groupList = (List) groups.get(new Integer(field));
        if (groupList == null) {
            groupList = new ArrayList();
            groups.put(new Integer(field), groupList);
        }
        return groupList;
    }

    public Group getGroup(int num, Group group) throws FieldNotFound {
        List groupList = getGroups(group.getField());
        if (num > groupList.size()) {
            throw new FieldNotFound(group.getField() + ", index=" + num);
        }
        group.setFields((Group) groupList.get(num - 1));
        return group;
    }
}