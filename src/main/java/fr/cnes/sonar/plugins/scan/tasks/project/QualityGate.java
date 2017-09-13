/*
 * This file is part of cnesscan.
 *
 * cnesscan is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * cnesscan is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with cnesscan.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.cnes.sonar.plugins.scan.tasks.project;

/**
 * Model of Quality Gate for json parsing
 * @author lequal
 */
public class QualityGate {

    /**
     * Id of the quality gate
     */
    private String id;

    /**
     * Id of the quality gate
     */
    private String name;

    /**
     * Default constructor
     */
    public QualityGate() {
        this.id = "";
        this.name = "";
    }

    /**
     * Get id of the quality gate
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the id
     * @param pId id to set
     */
    public void setId(final String pId) {
        this.id = pId;
    }

    /**
     * Get name of the quality gate
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name
     * @param pName name to set
     */
    public void setName(final String pName) {
        this.name = pName;
    }
}
