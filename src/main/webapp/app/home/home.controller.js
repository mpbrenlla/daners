(function() {
    'use strict';

    angular
        .module('danersApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$uibModal','DateEvent','ParseLinks'];

    function HomeController ($scope, Principal, LoginService, $state, $uibModal,DateEvent,ParseLinks) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;

        vm.eventSource = [];

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        loadEvents();
        
        vm.onTimeSelected = function (selectedTime) {
            console.log("Fecha seleccionada");
            console.log(selectedTime);
        
            vm.open(selectedTime);
        };

        vm.onEventSelected = function (event) {
            console.log("Evento seleccionado");
            console.log(event.startTime);
        };

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        vm.open = function(date) {
            $state.go('date-event.new',{date:date});
        }

        function loadEvents() {
            DateEvent.query(onSuccess, onError);
        }


        function onSuccess(data, headers) {
            console.log(data);
            angular.forEach(data, function(value, key) {

                var item = {
                    'startTime':value.date,
                    'endTime':value.date,
                    'title':'Event',
                    'allDay':true
            }

              this.push(item);
            }, vm.eventSource);
            $scope.$broadcast('eventSourceChanged',vm.eventSource)
        
            console.log(headers('X-Total-Count'));
            console.log(ParseLinks.parse(headers('link')));
               
            }
            function onError(error) {
                console.log(error.data.message);
            }
        
    }
})();
