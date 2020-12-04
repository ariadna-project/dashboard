package com.ud.iot.charts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.ud.iot.spring.components.DashWebClient;
import com.ud.iot.spring.components.ObjectStorage;
import com.ud.iot.util.ClientAttrs;
import com.ud.iot.util.ClientUtils;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public abstract class AbstractDataList<T> extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
	
	@WireVariable private ObjectStorage objectStorage;
	@WireVariable protected DashWebClient dashWebClient;
	
	@Wire private Listbox litbox;
	@Wire private Button btnLoadMore;
	
	protected Desktop desktop;
	
	protected long idDevice;
	private long totalPages;
	private long lastPageLoaded;
//	private List<T> loadedElements;
	private ListModelList<T> listModelList;

	protected abstract ListModelList<T> createListModelList();
	protected abstract long getTotalElementFromService();
	protected abstract void loadElementFromService(List<Long> pages);
	protected abstract String getDataName();
	protected abstract String getEventName();
	protected abstract Comparator<T> getElementComparator();

	protected long getPageSize() {
		return 300;
	}
	
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        this.desktop = Executions.getCurrent().getDesktop();
        if(!desktop.isServerPushEnabled()) {
        	desktop.enableServerPush(true);
        }
        
//        this.loadedElements = new ArrayList<>();
        this.listModelList = this.createListModelList();
        this.litbox.setModel(this.listModelList);
        this.idDevice = Long.valueOf((String)objectStorage.getObject(ClientAttrs.PARAM_ID_DEVICE));
        
        String queue = "updateData_" + this.idDevice;
        System.out.println("suscribiendo a " + queue);
        EventQueues.lookup(queue, EventQueues.DESKTOP, true).subscribe(new EventListener<Event>() {
        	@Override
         	public void onEvent(Event event) throws Exception {
        	}
        });

        new Thread(() -> {
        	long totalRegs = getTotalElementFromService();
        	if(totalRegs == 0) {
        		ClientUtils.sendEventToDesktop(desktop, getEventName(), new ArrayList<>());
        	} else {
        		totalPages = totalRegs / getPageSize();
        		if(totalPages > 0 &&
        				totalRegs % totalPages > 0) {
        			totalPages++;
        		}
        		List<Long> pages = new ArrayList<>();
        		pages.add(totalPages == 0? totalPages : totalPages-1);
        		loadData(pages, getPageSize());
        	}
        }).start();
        
        Clients.showBusy(litbox, "Obteniendo datos");
    }
    
    private void loadData(List<Long> pages, long pageSize) {
    	this.loadElementFromService(pages);
    	lastPageLoaded = pages.get(pages.size()-1);
    }
    
    public void onDataLoaded(Event event) {
    	Clients.clearBusy(litbox);
    	
    	List<T> data = new ArrayList<>((List<T>) event.getData());
    	if(data.size() == 0) {
    		return;
    	}
    	Collections.sort(data, this.getElementComparator());
//    	loadedElements.addAll(0, data);
    	this.listModelList.addAll(0, data);
    	this.showBtnLoadMore();
    }
    
    private void showBtnLoadMore() {
    	if(lastPageLoaded > 0) {
    		this.btnLoadMore.setVisible(true);
    	} else {
    		this.btnLoadMore.setVisible(false);
    	}
    }
    
    @Listen("onClick=#btnLoadMore")
    public void onClickBtnLoadMore(Event event) {
    	loadData(Arrays.asList(lastPageLoaded-1), getPageSize());
    }
}
