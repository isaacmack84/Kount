/*global angular */

/**
 * The main TodoMVC app module
 *
 * @type {angular.Module}
 */
angular.module('todomvc', ['ngRoute', 'ngResource','ngCookies'])
	.config(function ($routeProvider) {
		'use strict';

		var rootRouteConfig = {
			controller: 'RootCtrl',
			templateUrl: 'root-index.html'
		};
		
		var todoRouteConfig = {
			controller: 'TodoCtrl',
			templateUrl: 'todomvc-index.html',
			resolve: {
				store: function (todoStorage) {
					// Get the correct module (API or localStorage).
					return todoStorage.then(function (module) {
						module.get(); // Fetch the todo records in the background.
						return module;
					});
				}
			}
		};

		$routeProvider
			.when('/', rootRouteConfig)
			.when('/todos', todoRouteConfig)
			.when('/todos/:status', todoRouteConfig)
			.otherwise({
				redirectTo: '/'
			});
	});
