package area51.turboRocketWars.controllers;

import area51.turboRocketWars.Bodies.maps.Map;

public interface IMainController {

	Map[] getMaps();
	void selectMap(Map map);
	

}
