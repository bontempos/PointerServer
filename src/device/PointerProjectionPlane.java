package device;

import java.util.ArrayList;

import bontempos.ProjectionMatrix.HomographyMatrix;
import pointerserver.PointerCanvas;
import pointerserver.ProjectionMask;
import processing.core.PMatrix;
import processing.core.PVector;

public class PointerProjectionPlane {
	
	
	private final PointerDevice parentPointer;
	private PVector planeCenter;					//should be update by microcontroller 
	private float projectionApperture;
	private ArrayList<ProjectionMask> projMasks;
	
	private double[][] homographyMatrix = null;
	private PMatrix projectionMatrix = null;
	

	public PointerProjectionPlane( PointerDevice parentPointer ) {
		this.parentPointer = parentPointer;
		
		//below are going to be overwritten by environment setup 
		projectionApperture = 30;
		planeCenter = new PVector();
	}
	
	
	
	/*
	 *  builds a homography matrix considering two 4-sided polygons and their coplanar corners positions
	 *  @canvas the origin polygon;
	 *  @projMask the destination polygon
	 */
	public void buildHomograpyMatrix( PointerCanvas canvas, ProjectionMask projMask ){
		homographyMatrix = null;
		
		while (homographyMatrix == null) {
			PVector [] from = canvas.getCorners();
			PVector [] to   = projMask.getCorners();
			homographyMatrix = HomographyMatrix.get( from, to ); 
			
			if (homographyMatrix == null) {
				System.out.println("PointerDevice Class: Failed to create Homography plane");
			}
		}
	}
	
	
	
	/*
	 *  @positionInCanvas the x,y position to be converted
	 *  @canvas the origin polygon **;
	 *  @projMask the destination polygon **
	 *  if ** are used, a new homography matrix is build before conversion
	 *  @returns a converted x y position
	 */
	public PVector toHomography(PVector positionInCanvas){
		return HomographyMatrix.solve(positionInCanvas, homographyMatrix);
	}
	public PVector toHomography(PVector positionInCanvas, PointerCanvas canvas, ProjectionMask projMask){
		buildHomograpyMatrix(canvas,projMask);
		return toHomography(positionInCanvas);
	}
	
	
	
	/*
	 *  builds a projection matrix considering correspondent points from a 2D set of points in projection plane and a 3D model loaded on computer
	 *  @p2d a set of points from projection plane
	 *  @p3d a set of points from 3d model object
	 */
	public void buildProjectionMatrix(){}
}
