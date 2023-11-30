package dk.nykredit.pmp.tracker;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

public class Tracker {

	// The time in miliseconds within which the service must have been refreshed (1 min.).
	private final static int staleTimeLimit = 3600000;
	private static Timer timer;


	private static ArrayList<Environment> environments;

	public Tracker(){
		if (environments == null) {
			environments = new ArrayList<>();
		}

		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					pruneStaleServices(staleTimeLimit);
				}
			 }, 30000, staleTimeLimit);
		}
	}

	public ArrayList<Service> getServices(String environmentReq){

		if (findEnvironment(environmentReq) == null) return null;

		return findEnvironment(environmentReq).getServices();
	}

	public boolean serviceIsRegistered(String environmentReq, Service service) {

		return findEnvironment(environmentReq).getServices().contains(service);
	} 

	public Service getServiceFromAddress(String address, String environmentReq) {

		Environment environment = findEnvironment(environmentReq);

		if (environment == null) return null;


		for (Service service : findEnvironment(environmentReq).getServices()) {
			if (service.getPmpRoot().equals(address)) {
				return service;
			}
		}

		return null;
	}

	public void registerService(Service service, String environmentReq) {
		
		createEnvironment(environmentReq).getServices().add(service);
	}

	public ArrayList<Service> readServices(String environmentReq) {
		return findEnvironment(environmentReq).getServices();
	}

	/**
	 * Returns a list of all the environments that are registered.
	 * @return a list of all the environments that are registered.
	 */
	public ArrayList<String> getEnvironmentNames() {
		
		ArrayList<String> environmentNames = new ArrayList<>();

		for (Environment environment : environments) {
			environmentNames.add(environment.getEnvironmentName());
		}

		return environmentNames;
	
	}

	/**
	 * Removes all services that have gone too long without a refresh.
	 * Removes all environments that have no services.
	 * @param staleTimeLimit the time in miliseconds within which the service must have been refreshed.
	 */
	private void pruneStaleServices(int staleTimeLimit) {

		for (Environment environment : environments) {

			ListIterator<Service> iterator = environment.getServices().listIterator();
			
			while (iterator.hasNext()) {
				if (iterator.next().isStale(staleTimeLimit)) {
					iterator.remove();
				}
			}
		}

		pruneEmptyEnvironments();
	}

	private void pruneEmptyEnvironments() {

		ListIterator<Environment> envIterator = environments.listIterator();

		while (envIterator.hasNext()) {
			if (envIterator.next().getServices().isEmpty()) {
				envIterator.remove();
			}
		}
	}

	/**
	 * Finds an environment by name.
	 * @param envString the name of the environment to be found.
	 * @return the environment that was found or null if no environment was found.
	 */
	private Environment findEnvironment(String envString) {

		for (Environment environment : environments) {
			if (environment.getEnvironmentName().equals(envString)) {
				return environment;
			}
		}

		return null;

	}

	/**
	 * Creates a new environment if it does not already exist.
	 * @param envString the name of the environment to be created.
	 * @return the environment that was created or the existing environment.
	 */
	private Environment createEnvironment(String envString) {

		if (findEnvironment(envString) != null) {
			return findEnvironment(envString);
		}

		Environment newEnvironment = new Environment(envString);
		environments.add(newEnvironment);

		return newEnvironment;

	}
}