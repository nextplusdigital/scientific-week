(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('RemedioDeleteController',RemedioDeleteController);

    RemedioDeleteController.$inject = ['$uibModalInstance', 'entity', 'Remedio'];

    function RemedioDeleteController($uibModalInstance, entity, Remedio) {
        var vm = this;

        vm.remedio = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Remedio.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
