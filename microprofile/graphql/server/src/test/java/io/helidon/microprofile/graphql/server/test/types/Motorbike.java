/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
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

package io.helidon.microprofile.graphql.server.test.types;

import org.eclipse.microprofile.graphql.Description;

/**
 * Represents a Motorbike.
 */
@Description("A representation of a motorbike")
public class Motorbike extends AbstractVehicle {

    boolean hasSideCar;

    public Motorbike(String plate, int numberOfWheels, String model, String make, int getManufactureYear, boolean hasSideCar) {
        super(plate, numberOfWheels, model, make, getManufactureYear);
        this.hasSideCar = hasSideCar;
    }

    public boolean isHasSideCar() {
        return hasSideCar;
    }

    public void setHasSideCar(boolean hasSideCar) {
        this.hasSideCar = hasSideCar;
    }
}
