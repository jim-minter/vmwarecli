package uk.minter.vmwarecli;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class VMAPI extends ServiceInstance {
	public VMAPI(URL url, String username, String password, boolean ignoreCert) throws RemoteException, MalformedURLException {
		super(url, username, password, ignoreCert);
	}

	public String deleteVM(final String path) throws RuntimeFault, RemoteException, InterruptedException {
		VirtualMachine vm = (VirtualMachine)getSearchIndex().findByInventoryPath(path);
		return vm.destroy_Task().waitForTask();
	}
	
	public String cloneVMtoTemplate(final String srcpath, final String dstname) throws RuntimeFault, RemoteException, InterruptedException {
		VirtualMachine vm = (VirtualMachine)getSearchIndex().findByInventoryPath(srcpath);
		VirtualMachineCloneSpec spec = new VirtualMachineCloneSpec();
		spec.template = true;
		spec.location = new VirtualMachineRelocateSpec();
		
		return vm.cloneVM_Task((Folder)vm.getParent(), dstname, spec).waitForTask();
	}
}