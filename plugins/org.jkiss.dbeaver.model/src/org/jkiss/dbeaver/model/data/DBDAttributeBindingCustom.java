/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2019 Serge Rider (serge@jkiss.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jkiss.dbeaver.model.data;

import org.jkiss.code.NotNull;
import org.jkiss.code.Nullable;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.model.DBPDataKind;
import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBUtils;
import org.jkiss.dbeaver.model.exec.DBCAttributeMetaData;
import org.jkiss.dbeaver.model.exec.DBCException;
import org.jkiss.dbeaver.model.exec.DBCSession;
import org.jkiss.dbeaver.model.struct.DBSDataContainer;
import org.jkiss.dbeaver.model.struct.DBSEntityReferrer;
import org.jkiss.dbeaver.model.virtual.DBVEntityAttribute;
import org.jkiss.utils.CommonUtils;

import java.util.List;

/**
 * Virtual attribute value binding info
 */
public class DBDAttributeBindingCustom extends DBDAttributeBinding {
    @Nullable
    private final DBDAttributeBindingCustom parent;
    @NotNull
    private DBSDataContainer dataContainer;
    @NotNull
    private final DBVEntityAttribute vAttribute;
    private List<DBSEntityReferrer> referrers;
    private int ordinalPosition;

    public DBDAttributeBindingCustom(
        @Nullable DBDAttributeBindingCustom parent,
        @NotNull DBSDataContainer dataContainer,
        @NotNull DBPDataSource dataSource,
        @NotNull DBVEntityAttribute vAttribute,
        int ordinalPosition) {
        super(DBUtils.findValueHandler(dataSource, vAttribute));
        this.parent = parent;
        this.dataContainer = dataContainer;
        this.vAttribute = vAttribute;
        this.ordinalPosition = ordinalPosition;
    }

    @NotNull
    @Override
    public DBPDataSource getDataSource() {
        return dataContainer.getDataSource();
    }

    @Override
    public boolean isCustom() {
        return true;
    }

    @Nullable
    @Override
    public DBDAttributeBindingCustom getParentObject() {
        return parent;
    }

    /**
     * Attribute index in result set
     *
     * @return attribute index (zero based)
     */
    @Override
    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public boolean isAutoGenerated() {
        return true;
    }

    @NotNull
    @Override
    public DBSDataContainer getDataContainer() {
        return dataContainer;
    }

    @Override
    public String getTypeName() {
        return vAttribute.getTypeName();
    }

    @Override
    public String getFullTypeName() {
        return vAttribute.getFullTypeName();
    }

    @Override
    public int getTypeID() {
        return vAttribute.getTypeID();
    }

    @Override
    public DBPDataKind getDataKind() {
        return vAttribute.getDataKind();
    }

    @Override
    public Integer getScale() {
        return vAttribute.getScale();
    }

    @Override
    public Integer getPrecision() {
        return vAttribute.getPrecision();
    }

    @Override
    public long getMaxLength() {
        return vAttribute.getMaxLength();
    }

    /**
     * Attribute label
     */
    @NotNull
    public String getLabel() {
        return vAttribute.getName();
    }

    /**
     * Attribute name
     */
    @NotNull
    public String getName() {
        return vAttribute.getName();
    }

    /**
     * Meta attribute (obtained from result set)
     */
    @Nullable
    public DBCAttributeMetaData getMetaAttribute() {
        return null;
    }

    /**
     * Entity attribute (may be null)
     */
    @NotNull
    public DBVEntityAttribute getEntityAttribute() {
        return vAttribute;
    }

    /**
     * Row identifier (may be null)
     */
    @Nullable
    public DBDRowIdentifier getRowIdentifier() {
        return null;
    }

    @Override
    public String getRowIdentifierStatus() {
        return "Virtual attribute";
    }

    @Nullable
    @Override
    public List<DBSEntityReferrer> getReferrers() {
        return referrers;
    }

    @Nullable
    @Override
    public Object extractNestedValue(@NotNull Object ownerValue) throws DBCException {
        throw new DBCException("Meta binding doesn't support nested values");
    }

    @Override
    public void lateBinding(@NotNull DBCSession session, List<Object[]> rows) throws DBException {
        referrers = findVirtualReferrers();
        super.lateBinding(session, rows);
    }

    @Override
    public String toString() {
        return vAttribute.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DBDAttributeBindingCustom) {
            DBVEntityAttribute cmpMeta = ((DBDAttributeBindingCustom) obj).vAttribute;
            return CommonUtils.equalObjects(vAttribute.getName(), cmpMeta.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return CommonUtils.notEmpty(vAttribute.getName()).hashCode();
    }

}
