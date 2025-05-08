import { useEffect, useState } from 'react';
import {
  View,
  StyleSheet,
  Button,
  Text,
  PermissionsAndroid,
} from 'react-native';
import {
  checkAccessibilityPermission,
  superTrackingServiceSettingsEnabled,
  superTrackingServiceStatus,
} from 'react-native-background-service-tracking';
import { startBackgroundTrackingCommand } from '../backgroundTrackingServiceCommands/startBackgroundTrackingServiceCommand';
import { stopBackgroundServiceCommand } from '../backgroundTrackingServiceCommands/stopBackgroundTrackingServiceCommand';

export default function App() {
  const [isServiceRunning, setIsServiceRunning] = useState(false);

  useEffect(() => {
    const subscriber = superTrackingServiceStatus((isServiceRunning) => {
      setIsServiceRunning(isServiceRunning);
    });
    return () => subscriber.remove();
  }, []);

  useEffect(() => {
    const subscriber = superTrackingServiceSettingsEnabled(
      (isGlobalSettingsEnabled) => {
        console.log(`global settings enabled ${isGlobalSettingsEnabled}`);
      }
    );
    return () => subscriber.remove();
  }, []);

  useEffect(() => {
    checkAccessibilityPermission().then((value) => {
      setIsServiceRunning(value);
    });
  }, []);

  useEffect(() => {
    const notificationPermission = async () => {
      const gotNotificationPermission = await PermissionsAndroid.check(
        PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS
      );
      if (!gotNotificationPermission) {
        const permissionRequestResult = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS
        );
        if (permissionRequestResult === PermissionsAndroid.RESULTS.DENIED) {
          notificationPermission();
        }
      }
    };
    notificationPermission();
  }, []);

  return (
    <View style={styles.container}>
      <View style={{ marginVertical: 8, gap: 8 }}>
        <Button
          title="Start Tracking"
          onPress={() =>
            startBackgroundTrackingCommand(() => {
              console.log('Service started');
              setIsServiceRunning(true);
            })
          }
          disabled={isServiceRunning}
        />
        <Button
          title="Stop Tracking"
          onPress={() =>
            stopBackgroundServiceCommand(() => setIsServiceRunning(false))
          }
          disabled={!isServiceRunning}
        />
      </View>
      <Text>{isServiceRunning ? 'Service Running' : 'Service Stopped'}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
