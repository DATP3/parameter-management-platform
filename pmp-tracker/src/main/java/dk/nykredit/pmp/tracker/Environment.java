package dk.nykredit.pmp.tracker;

import java.util.ArrayList;

public class Environment {
    private String environmentName;
    private ArrayList<Service> services; 

    public Environment() {
        if (services == null) {
            services = new ArrayList<>();
        }
    }

    public Environment(String environmentName) {
        this.environmentName = environmentName;
        if (services == null) {
            services = new ArrayList<>();
        }
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Environment) {
            return ((Environment) obj).getEnvironmentName().equals(environmentName);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return environmentName.hashCode();
    }
}
