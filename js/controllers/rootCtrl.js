/*global angular */

/**
 * The login controller for the app
 */
angular.module('todomvc')
	.controller('RootCtrl', function TodoCtrl($http, $cookies, $scope) {
		'use strict';

		$scope.users = [];
		
		$http.get('/api/users')
			.then(function(response) {
				$scope.users = response.data;
			})
			;
		
		$scope.setuser = function(user) {
			$cookies.put('user',user,{path:'/'});
			window.location.href = '#!/todos';
		};
		
		$scope.addUser = function() {
			$http.post('/api/users',$scope.newUser);
			$scope.users.push($scope.newUser);
			$scope.setuser($scope.newUser);
		};
	});
