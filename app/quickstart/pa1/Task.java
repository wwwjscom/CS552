package app.quickstart.pa1;

public class Task {
	int c, t, r, d;
	
	Task(int c, int t, int r, int d) {
		this.c = c;
		this.t = t;
		this.r = r;
		this.d = d;
	}
	
	// Returns the computation time
	public int c() {
		return c;
	}
	
	public boolean c(int new_c) {
		this.c = new_c;
		return true;
	}
	
	// Returns the period
	public int t() {
		return t;
	}
	
	// Returns the release time
	public int r() {
		return r;
	}
	
	// Returns the deadline
	public int d() {
		return d;
	}
}
