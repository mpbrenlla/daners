(function() {
    'use strict';

    angular
        .module('danersApp')
        .controller('DateEventDeleteController',DateEventDeleteController);

    DateEventDeleteController.$inject = ['$uibModalInstance', 'entity', 'DateEvent'];

    function DateEventDeleteController($uibModalInstance, entity, DateEvent) {
        var vm = this;

        vm.dateEvent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DateEvent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
