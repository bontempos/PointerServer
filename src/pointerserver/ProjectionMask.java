/*
 *  binds a Pointer device with a surface plane
 *  A mask is a 4 corners shape 2D object representing the surface plane.
 *  This mask size is constrained by the resolution of the motors (180º by 180º)
 */

package pointerserver;

import device.PointerDevice;
import processing.core.PVector;

public class ProjectionMask {
	
	//homographic plane for 2D projection
	private PVector corners [] 	= new PVector[4];
	
	//correspondent points for 3D projection
	private int correspondentPoints = 6;
	private PVector P2D[]		= new PVector[correspondentPoints];
	
	//offset points
	private PVector offset = new PVector();

	//related objects
	private PointerDevice pointer;
	private ProjectionSurface surface;
	

	
	
	public ProjectionMask(PointerDevice pointer) {
		this.pointer = pointer;
		setDefaultCorners();
		setDefaultP2D();
	}
	
	
	
	public ProjectionMask(PointerDevice pointer, ProjectionSurface surface) {
		this.pointer = pointer;
		this.surface = surface;
		setDefaultCorners();
		setDefaultP2D();
	}

	
	
	/*
	 *  get offset
	 */
	public PVector getOffset( ){
		return offset;
	}
	
	
	
	/*
	 *  sets offset
	 *  @offset
	 */
	public PVector setOffset( PVector offset ){
		return this.offset = offset;
	}
	
	
	
	/*
	 *  sets surface
	 *  @surface
	 */
	public ProjectionSurface setSurface( ProjectionSurface surface ){
		return this.surface = surface;
	}
	
	
	

	/*
	 *  get corners
	 */
	public PVector[] getCorners( ){
		return corners;
	}
	
	
	
	
	
	/*
	 *  loads default corner state, representing 
	 */
	public void setDefaultCorners(){
		corners[0] = new PVector();
		corners[1] = new PVector(0,180);
		corners[2] = new PVector(180,180);
		corners[3] = new PVector(0,180);
	}
	
	
	
	/*
	 *  set to zero 2d correspondent points
	 */
	public void setDefaultP2D(){
		for (int i = 0; i < correspondentPoints; i++){
			P2D[i] = new PVector();
		}
	}
}
