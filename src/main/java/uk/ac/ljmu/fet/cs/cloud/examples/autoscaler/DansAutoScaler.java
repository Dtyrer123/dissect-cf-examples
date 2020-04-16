package uk.ac.ljmu.fet.cs.cloud.examples.autoscaler;

import java.util.ArrayList;
import java.util.Iterator;

import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine.State;

public class DansAutoScaler extends VirtualInfrastructure {

	public DansAutoScaler(IaaSService cloud) {
		super(cloud);
	}

	@Override
	public void tick(long fires) {

		// a list of applications that need a virtual infrastructure
		final Iterator<String> kinds = vmSetPerKind.keySet().iterator();
		while (kinds.hasNext()) {
			final String types = kinds.next();
			final ArrayList<VirtualMachine> vmset = vmSetPerKind.get(types);
			if (vmset.size() > 0) {
				if (vmset.size() < 1) {
					requestVM(types);

				}
			} else if (vmset.isEmpty()) {
				requestVM(types);
			} else
				for (VirtualMachine vm : vmset) {
					if (vm.getState().equals(State.RUNNING)) {
						// Destroys the virtual machine if it has no tasks or is empty
						if (vm.underProcessing.isEmpty() && vm.toBeAdded.isEmpty()) {
							System.out.println("VM Destroyed");
							destroyVM(vm);

						}
					}
				}
		}

	}
}
