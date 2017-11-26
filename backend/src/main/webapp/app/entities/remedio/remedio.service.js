(function() {
    'use strict';
    angular
        .module('romaninisistemasApp')
        .factory('Remedio', Remedio);

    Remedio.$inject = ['$resource'];

    function Remedio ($resource) {
        var resourceUrl =  'api/remedios/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
