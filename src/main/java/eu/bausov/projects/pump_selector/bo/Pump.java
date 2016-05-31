package eu.bausov.projects.pump_selector.bo;

import eu.bausov.projects.pump_selector.utils.units_and_convertors.Capacity;
import eu.bausov.projects.pump_selector.utils.units_and_convertors.Pressure;
import eu.bausov.projects.pump_selector.utils.units_and_convertors.Temperature;

public abstract class Pump extends Equipment {
    Material castingMaterial;
    Material shaftMaterial;
    Seal seal;
    Capacity capacity;
    Pressure pressure;
    Temperature maxTemperature;
}
