(function() {
    'use strict';

    angular
        .module('danersApp')
        .controller('DateEventDialogController', DateEventDialogController);

    DateEventDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DateEvent', 'User'];

    function DateEventDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DateEvent, User) {
        var vm = this;

        vm.dateEvent = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dateEvent.id !== null) {
                DateEvent.update(vm.dateEvent, onSaveSuccess, onSaveError);
            } else {
                DateEvent.save(vm.dateEvent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('danersApp:dateEventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
