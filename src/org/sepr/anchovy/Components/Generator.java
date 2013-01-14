package org.sepr.anchovy.Components;

import java.util.ArrayList;
import java.util.Iterator;

import org.sepr.anchovy.InfoPacket;
import org.sepr.anchovy.Pair;
import org.sepr.anchovy.Pair.Label;

/**
 * This class the the representation of a Generator within the power plant. 
 * @author Harrison
 *
 */
public class Generator extends Component {
	private double electrisityGenerated = 0;
	private double generationRatio = 1.5;
	/**
	 * Set up the generator, mostly done in the Component.
	 * @param name The unique name of the component. 
	 */
	public Generator(String name){
		super(name);
	}
	
	@Override
	public InfoPacket getInfo() {
		InfoPacket info = super.getSuperInfo();
		info.namedValues.add(new Pair<Double>(Label.elec, electrisityGenerated));
		return info;
	}

	@Override
	public void calucalte() {
		super.setOuputFlowRate(calculateOutputFlowRate());
		electrisityGenerated += super.getOutputFlowRate();
	}

	@Override
	/**
	 * The generator is the part of the power plant that generates the electricity
	 * The output flow rate of the generator is the amount of amount of electisity being generated per cycle.
	 * 
	 * The generation of electricity is proportional to the RPM of the turbine attached to it, not the ouput flow rate of the turbines..
	 * 
	 * @return amount of electricity generated in current cycle.
	 */
	protected double calculateOutputFlowRate() {
		ArrayList<Component> inputComponents = super.getRecievesInputFrom();
		double totalInputRPM = 0;
		Iterator<Component> it = inputComponents.iterator();
		Component comp = null;
		Turbine turbineIn = null;
		
		while(it.hasNext()){
			comp = it.next();
			if(comp instanceof Turbine){
				turbineIn = (Turbine) comp;
				totalInputRPM += turbineIn.getRPM();
			}
		}
		
		return totalInputRPM * getGenerationRatio();
	}

	@Override
	public void takeInfo(InfoPacket info) throws Exception {
		super.takeSuperInfo(info);
		Iterator<Pair<?>> i = info.namedValues.iterator();
		Pair<?> pair = null;
		Label label = null;
		while(i.hasNext()){
			pair = i.next();
			label = pair.getLabel();
			switch (label){
			case elec:
				electrisityGenerated = (Double) pair.second();
			default:
				break;
			}
		}

	}

	/**
	 * Rather than just the output flow rate, Generator also contains the total amount of electricity that has been generated by the plant.
	 * @return The total amount of electrisy generated by the plant.
	 */
	public double getElectrisityGenerated() {
		return electrisityGenerated;
	}

	public void setElectrisityGenerated(double electrisityGenerated) {
		this.electrisityGenerated = electrisityGenerated;
	}

	public double getGenerationRatio() {
		return generationRatio;
	}

	public void setGenerationRatio(double generationRatio) {
		this.generationRatio = generationRatio;
	}

}
