(function() {
    'use strict';

    angular
        .module('danersApp')
        .controller('DateEventDetailController', DateEventDetailController);

    DateEventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'DateEvent', 'User'];

    function DateEventDetailController($scope, $rootScope, $stateParams, entity, DateEvent, User) {
        var vm = this;

        vm.dateEvent = entity;

        var unsubscribe = $rootScope.$on('danersApp:dateEventUpdate', function(event, result) {
            vm.dateEvent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
