(function() {
    'use strict';
    angular
        .module('noctemApp')
        .factory('Record', Record);

    Record.$inject = ['$resource', 'DateUtils'];

    function Record ($resource, DateUtils) {
        var resourceUrl =  'api/records/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
